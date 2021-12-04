package com.km.common.api_impl

import com.km.common.net.KmBaseUrl
import io.ktor.client.*
import io.ktor.client.features.websocket.*
import io.ktor.http.cio.websocket.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

open class Wss(
    path: String,
    private val httpClient: HttpClient,
    private val scope: CoroutineScope
) {
    private lateinit var webSocketSession: DefaultWebSocketSession

    val flow = flow {
        httpClient.wss(host = KmBaseUrl.host, port = KmBaseUrl.port, path = path) {
            println("wss 连接成功")
            webSocketSession = this

            while (isActive) {
                val othersMessage = incoming.receive() as? Frame.Text
                othersMessage?.readText()?.also {
                    emit(it)
                }
            }
        }
    }.shareIn(scope, SharingStarted.Lazily,1)


    fun send(text: String) {
        scope.launch {
            webSocketSession.send(Frame.Text(text))
        }
    }

    fun close(reason: CloseReason = CloseReason(CloseReason.Codes.NORMAL, "")) {
        scope.launch{
            webSocketSession.close(reason)
        }
    }

}