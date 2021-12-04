package com.km.common.net

import io.ktor.client.engine.*
import io.ktor.client.features.*
import io.ktor.client.features.websocket.*
import io.ktor.client.fetch.*
import io.ktor.client.request.*
import io.ktor.client.utils.*
import io.ktor.http.*
import io.ktor.http.Headers
import io.ktor.http.cio.websocket.*
import io.ktor.http.content.*
import io.ktor.util.*
import io.ktor.util.date.*
import io.ktor.utils.io.*
import io.ktor.utils.io.core.*
import kotlinx.coroutines.*
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.channels.consumeEach
import org.khronos.webgl.ArrayBuffer
import org.khronos.webgl.Int8Array
import org.khronos.webgl.Uint8Array
import org.w3c.dom.ARRAYBUFFER
import org.w3c.dom.BinaryType
import org.w3c.dom.MessageEvent
import org.w3c.dom.WebSocket
import org.w3c.dom.events.Event
import org.w3c.fetch.FOLLOW
import org.w3c.fetch.RequestRedirect
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.js.Promise


@OptIn(InternalAPI::class)
internal class JsClientEngine(override val config: HttpClientEngineConfig) : HttpClientEngineBase("ktor-js") {

    override val dispatcher = Dispatchers.Default

    override val supportedCapabilities = setOf(HttpTimeout, WebSocketCapability)

    init {
        println("RequestInitJS")
        check(config.proxy == null) { "Proxy unsupported in Js engine." }
    }


    override suspend fun execute(data: HttpRequestData): HttpResponseData {
        val callContext = callContext()

        if (data.isUpgradeRequest()) {
            return executeWebSocketRequest(data, callContext)
        }

        val requestTime = GMTDate()
        val rawRequest = data.toRaw(callContext)
        val rawResponse = commonFetch(data.url.toString(), rawRequest)

        val status = HttpStatusCode(rawResponse.status.toInt(), rawResponse.statusText)
        val headers = rawResponse.headers.mapToKtor()
        val version = HttpProtocolVersion.HTTP_1_1

        val body = CoroutineScope(callContext).readBodyBrowser(rawResponse)

        return HttpResponseData(
            status,
            requestTime,
            headers,
            version,
            body,
            callContext
        )
    }

    // Adding "_capturingHack" to reduce chances of JS IR backend to rename variable,
    // so it can be accessed inside js("") function
    private fun createWebSocket(urlString_capturingHack: String, headers: Headers): WebSocket {
        return if (PlatformUtils.IS_NODE) {
            val ws_capturingHack = js("eval('require')('ws')")
            val headers_capturingHack: dynamic = object {}
            headers.forEach { name, values ->
                headers_capturingHack[name] = values.joinToString(",")
            }
            js("new ws_capturingHack(urlString_capturingHack, { headers: headers_capturingHack } )")
        } else {
            js("new WebSocket(urlString_capturingHack)")
        }
    }

    private suspend fun executeWebSocketRequest(
        request: HttpRequestData,
        callContext: CoroutineContext,
    ): HttpResponseData {
        val requestTime = GMTDate()

        val urlString = request.url.toString()
        val socket: WebSocket = createWebSocket(urlString, request.headers)

        try {
            socket.awaitConnection()
        } catch (cause: Throwable) {
            callContext.cancel(CancellationException("Failed to connect to $urlString", cause))
            throw cause
        }

        val session = JsWebSocketSession(callContext, socket)

        return HttpResponseData(
            HttpStatusCode.OK,
            requestTime,
            Headers.Empty,
            HttpProtocolVersion.HTTP_1_1,
            session,
            callContext
        )
    }
}

@OptIn(InternalAPI::class, DelicateCoroutinesApi::class)
private suspend fun HttpRequestData.toRaw(callContext: CoroutineContext): RequestInit {
    val jsHeaders = js("({})")
    mergeHeaders(this@toRaw.headers, this@toRaw.body) { key, value ->
        jsHeaders[key] = value
    }

    val bodyBytes = when (val content = body) {
        is OutgoingContent.ByteArrayContent -> content.bytes()
        is OutgoingContent.ReadChannelContent -> content.readFrom().readRemaining().readBytes()
        is OutgoingContent.WriteChannelContent -> {
            GlobalScope.writer(callContext) {
                content.writeTo(channel)
            }.channel.readRemaining().readBytes()
        }
        else -> null
    }

    return buildObject {
        method = this@toRaw.method.value
        headers = jsHeaders
        redirect = RequestRedirect.FOLLOW

        //该文件从库源码复制而来，唯一目的是修改此参数
        credentials = "include"

        bodyBytes?.let { body = Uint8Array(it.toTypedArray()) }
    }
}

private  fun <T> buildObject(block: T.() -> Unit): T = (js("{}") as T).apply(block)

suspend fun commonFetch(
    input: String,
    init: RequestInit
): Response = suspendCancellableCoroutine { continuation ->
    val controller = AbortController()
    init.signal = controller.signal

    continuation.invokeOnCancellation {
        controller.abort()
    }

    val promise: Promise<Response> = if (PlatformUtils.IS_BROWSER) {
        fetch(input, init)
    } else {
        jsRequireNodeFetch()(input, init)
    }

    promise.then(
        onFulfilled = {
            continuation.resumeWith(Result.success(it))
        },
        onRejected = {
            continuation.resumeWith(Result.failure(Error("Fail to fetch", it)))
        }
    )
}

private  fun AbortController(): AbortController {
    return if (PlatformUtils.IS_BROWSER) {
        js("new AbortController()")
    } else {
        val controller = js("eval('require')('abort-controller')")
        js("new controller()")
    }
}


private fun CoroutineScope.readBodyBrowser(response: Response): ByteReadChannel {
    @Suppress("UNCHECKED_CAST_TO_EXTERNAL_INTERFACE")
    val stream = response.body as? ReadableStream ?: error("Fail to obtain native stream: ${response.asDynamic()}")
    return channelFromStream(stream)
}

private  fun CoroutineScope.channelFromStream(
    stream: ReadableStream
): ByteReadChannel = writer {
    val reader = stream.getReader()
    while (true) {
        try {
            val chunk = reader.readChunk() ?: break
            channel.writeFully(chunk.asByteArray())
        } catch (cause: Throwable) {
            reader.cancel(cause)
            throw cause
        }
    }
}.channel

private  external interface ReadableStream {
    public fun getReader(): ReadableStreamReader
}

private  external interface ReadResult {
    val done: Boolean
    val value: Uint8Array?
}

private  external interface ReadableStreamReader {
    public fun cancel(reason: dynamic): Promise<dynamic>
    public fun read(): Promise<ReadResult>
}

private  suspend fun ReadableStreamReader.readChunk(): Uint8Array? = suspendCancellableCoroutine { continuation ->
    read().then {
        val chunk = it.value
        val result = if (it.done || chunk == null) null else chunk
        continuation.resumeWith(Result.success(result))
    }.catch { cause ->
        continuation.resumeWithException(cause)
    }
}

@Suppress("UnsafeCastFromDynamic")
private  fun Uint8Array.asByteArray(): ByteArray {
    return Int8Array(buffer, byteOffset, length).asDynamic()
}


private fun jsRequireNodeFetch(): dynamic = try {
    js("eval('require')('node-fetch')")
} catch (cause: dynamic) {
    throw Error("Error loading module 'node-fetch': $cause")
}


private suspend fun WebSocket.awaitConnection(): WebSocket = suspendCancellableCoroutine { continuation ->
    if (continuation.isCancelled) return@suspendCancellableCoroutine

    val eventListener = { event: Event ->
        when (event.type) {
            "open" -> continuation.resume(this)
            "error" -> continuation.resumeWithException(WebSocketException(JSON.stringify(event)))
        }
    }

    addEventListener("open", callback = eventListener)
    addEventListener("error", callback = eventListener)

    continuation.invokeOnCancellation {
        removeEventListener("open", callback = eventListener)
        removeEventListener("error", callback = eventListener)

        if (it != null) {
            this@awaitConnection.close()
        }
    }
}

private fun io.ktor.client.fetch.Headers.mapToKtor(): Headers = buildHeaders {
    this@mapToKtor.asDynamic().forEach { value: String, key: String ->
        append(key, value)
    }
    Unit
}


@OptIn(InternalAPI::class)
class JsWebSocketSession(
    override val coroutineContext: CoroutineContext,
    private val websocket: WebSocket
) : DefaultWebSocketSession {
    private val _closeReason: CompletableDeferred<CloseReason> = CompletableDeferred()
    private val _incoming: Channel<Frame> = Channel(Channel.UNLIMITED)
    private val _outgoing: Channel<Frame> = Channel(Channel.UNLIMITED)

    override val incoming: ReceiveChannel<Frame> = _incoming
    override val outgoing: SendChannel<Frame> = _outgoing

    @ExperimentalWebSocketExtensionApi
    override val extensions: List<WebSocketExtension<*>> get() = emptyList()

    override val closeReason: Deferred<CloseReason?> = _closeReason

    override var maxFrameSize: Long
        get() = Long.MAX_VALUE
        set(value) {}

    init {
        websocket.binaryType = BinaryType.ARRAYBUFFER

        websocket.addEventListener(
            "message",
            callback = {
                val event = it.unsafeCast<MessageEvent>()

                launch {
                    val data = event.data

                    val frame: Frame = when (data) {
                        is ArrayBuffer -> Frame.Binary(false, Int8Array(data).unsafeCast<ByteArray>())
                        is String -> Frame.Text(data)
                        else -> {
                            val error = IllegalStateException("Unknown frame type: ${event.type}")
                            _closeReason.completeExceptionally(error)
                            throw error
                        }
                    }

                    _incoming.trySend(frame).isSuccess
                }
            }
        )

        websocket.addEventListener(
            "error",
            callback = {
                val cause = WebSocketException("$it")
                _closeReason.completeExceptionally(cause)
                _incoming.close(cause)
                _outgoing.cancel()
            }
        )

        websocket.addEventListener(
            "close",
            callback = { event: dynamic ->
                launch {
                    val reason = CloseReason(event.code as Short, event.reason as String)
                    _closeReason.complete(reason)
                    _incoming.send(Frame.Close(reason))
                    _incoming.close()

                    _outgoing.cancel()
                }
            }
        )

        launch {
            @OptIn(ExperimentalCoroutinesApi::class)
            _outgoing.consumeEach {
                when (it.frameType) {
                    FrameType.TEXT -> {
                        val text = it.data
                        websocket.send(String(text))
                    }
                    FrameType.BINARY -> {
                        val source = it.data as Int8Array
                        val frameData = source.buffer.slice(
                            source.byteOffset,
                            source.byteOffset + source.byteLength
                        )

                        websocket.send(frameData)
                    }
                    FrameType.CLOSE -> {
                        val data = buildPacket { writeFully(it.data) }
                        val code = data.readShort()
                        val reason = data.readText()
                        _closeReason.complete(CloseReason(code, reason))
                        if (code.isReservedStatusCode()) {
                            websocket.close()
                        } else {
                            websocket.close(code, reason)
                        }
                    }
                    FrameType.PING, FrameType.PONG -> {
                        // ignore
                    }
                }
            }
        }

        coroutineContext[Job]?.invokeOnCompletion { cause ->
            if (cause == null) {
                websocket.close()
            } else {
                websocket.close(CloseReason.Codes.INTERNAL_ERROR.code, "Client failed")
            }
        }
    }

    @OptIn(ExperimentalWebSocketExtensionApi::class)
    override fun start(negotiatedExtensions: List<WebSocketExtension<*>>) {
        require(negotiatedExtensions.isEmpty()) { "Extensions are not supported." }
    }

    override suspend fun flush() {
    }

    @Deprecated(
        "Use cancel() instead.",
        ReplaceWith("cancel()", "kotlinx.coroutines.cancel")
    )
    override fun terminate() {
        _incoming.cancel()
        _outgoing.cancel()
        _closeReason.cancel("WebSocket terminated")
        websocket.close()
    }


    private fun Short.isReservedStatusCode(): Boolean {
        return CloseReason.Codes.byCode(this).let { resolved ->
            @Suppress("DEPRECATION")
            resolved == null || resolved == CloseReason.Codes.CLOSED_ABNORMALLY
        }
    }
}

