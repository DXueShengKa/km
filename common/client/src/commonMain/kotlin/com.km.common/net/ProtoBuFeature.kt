package com.km.common.net

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.features.*
import io.ktor.client.features.json.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.client.utils.*
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.util.*
import io.ktor.util.reflect.TypeInfo
import io.ktor.utils.io.*
import io.ktor.utils.io.core.*

public fun defaultSerializer(): ProtoBufSerializer{
    return null!!
}

class ProtoBufContentTypeMatcher : ContentTypeMatcher {
    override fun contains(contentType: ContentType): Boolean {
        if (ContentType.Application.ProtoBuf.match(contentType)) {
            return true
        }

        val value = contentType.withoutParameters().toString()
        return value.startsWith("application/") && value.endsWith("+protobuf")
    }
}


/**
 * [HttpClient] feature that serializes/de-serializes as JSON custom objects
 * to request and from response bodies using a [serializer].
 *
 * The default [serializer] is [GsonSerializer].
 *
 * The default [acceptContentTypes] is a list which contains [ContentType.Application.Json]
 *
 * Note: It will de-serialize the body response if the specified type is a public accessible class
 *       and the Content-Type is one of [acceptContentTypes] list (`application/json` by default).
 *
 * @property serializer that is used to serialize and deserialize request/response bodies
 * @property acceptContentTypes that are allowed when receiving content
 */
public class ProtoBufFeature internal constructor(
    public val serializer: ProtoBufSerializer,
    public val acceptContentTypes: List<ContentType> = listOf(ContentType.Application.ProtoBuf),
    private val receiveContentTypeMatchers: List<ContentTypeMatcher> = listOf( ProtoBufContentTypeMatcher()),
) {
    @Deprecated("Install feature properly instead of direct instantiation.", level = DeprecationLevel.ERROR)
    public constructor(serializer: ProtoBufSerializer) : this(serializer, listOf(ContentType.Application.ProtoBuf))

    internal constructor(config: Config) : this(
        config.serializer ?: defaultSerializer(),
        config.acceptContentTypes,
        config.receiveContentTypeMatchers
    )

    /**
     * [JsonFeature] configuration that is used during installation
     */
    public class Config {
        /**
         * Serializer that will be used for serializing requests and deserializing response bodies.
         *
         * Default value for [serializer] is [defaultSerializer].
         */
        public var serializer: ProtoBufSerializer? = null

        /**
         * Backing field with mutable list of content types that are handled by this feature.
         */
        private val _acceptContentTypes: MutableList<ContentType> = mutableListOf(ContentType.Application.Json)
        private val _receiveContentTypeMatchers: MutableList<ContentTypeMatcher> =
            mutableListOf(ProtoBufContentTypeMatcher())

        /**
         * List of content types that are handled by this feature.
         * It also affects `Accept` request header value.
         * Please note that wildcard content types are supported but no quality specification provided.
         */
        public var acceptContentTypes: List<ContentType>
            set(value) {
                require(value.isNotEmpty()) { "At least one content type should be provided to acceptContentTypes" }

                _acceptContentTypes.clear()
                _acceptContentTypes.addAll(value)
            }
            get() = _acceptContentTypes

        /**
         * List of content type matchers that are handled by this feature.
         * Please note that wildcard content types are supported but no quality specification provided.
         */
        public var receiveContentTypeMatchers: List<ContentTypeMatcher>
            set(value) {
                require(value.isNotEmpty()) { "At least one content type should be provided to acceptContentTypes" }
                _receiveContentTypeMatchers.clear()
                _receiveContentTypeMatchers.addAll(value)
            }
            get() = _receiveContentTypeMatchers

        /**
         * Adds accepted content types. Be aware that [ContentType.Application.Json] accepted by default is removed from
         * the list if you use this function to provide accepted content types.
         * It also affects `Accept` request header value.
         */
        public fun accept(vararg contentTypes: ContentType) {
            _acceptContentTypes += contentTypes
        }

        /**
         * Adds accepted content types. Existing content types will not be removed.
         */
        public fun receive(matcher: ContentTypeMatcher) {
            _receiveContentTypeMatchers += matcher
        }
    }

    internal fun canHandle(contentType: ContentType): Boolean {
        val accepted = acceptContentTypes.any { contentType.match(it) }
        val matchers = receiveContentTypeMatchers

        return accepted || matchers.any { matcher -> matcher.contains(contentType) }
    }

    /**
     * Companion object for feature installation
     */
    public companion object Feature : HttpClientFeature<Config, ProtoBufFeature> {
        override val key: AttributeKey<ProtoBufFeature> = AttributeKey("Json")

        override fun prepare(block: Config.() -> Unit): ProtoBufFeature {
            val config = Config().apply(block)
            val serializer = config.serializer ?: defaultSerializer()
            val allowedContentTypes = config.acceptContentTypes.toList()
            val receiveContentTypeMatchers = config.receiveContentTypeMatchers

            return ProtoBufFeature(serializer, allowedContentTypes, receiveContentTypeMatchers)
        }

        override fun install(feature: ProtoBufFeature, scope: HttpClient) {
            scope.requestPipeline.intercept(HttpRequestPipeline.Transform) { payload ->
                feature.acceptContentTypes.forEach { context.accept(it) }

                val contentType = context.contentType() ?: return@intercept
                if (!feature.canHandle(contentType)) return@intercept

                context.headers.remove(HttpHeaders.ContentType)

                val serializedContent = when (payload) {
                    Unit -> EmptyContent
                    is EmptyContent -> EmptyContent
                    else -> feature.serializer.write(payload, contentType)
                }

                proceedWith(serializedContent)
            }

            scope.responsePipeline.intercept(HttpResponsePipeline.Transform) { (info, body) ->
                if (body !is ByteReadChannel) return@intercept

                val contentType = context.response.contentType() ?: return@intercept
                if (!feature.canHandle(contentType)) return@intercept

                val parsedBody = feature.serializer.read(info, body.readRemaining())
                val response = HttpResponseContainer(info, parsedBody)
                proceedWith(response)
            }
        }
    }
}


public interface ProtoBufSerializer {
    /**
     * Convert data object to [OutgoingContent].
     */
    public fun write(data: Any, contentType: ContentType): OutgoingContent

    /**
     * Convert data object to [OutgoingContent].
     */
    public fun write(data: Any): OutgoingContent = write(data, ContentType.Application.Json)

    /**
     * Read content from response using information specified in [type].
     */
    @Deprecated("Please use overload with io.ktor.util.reflect.TypeInfo parameter")
    public fun read(type: io.ktor.client.call.TypeInfo, body: Input): Any = read(type as TypeInfo, body)

    /**
     * Read content from response using information specified in [type].
     */
    public fun read(type: TypeInfo, body: Input): Any =
        read(TypeInfo(type.type, type.reifiedType, type.kotlinType), body)
}

