package com.km.common.net


import io.ktor.client.*
import io.ktor.client.features.*
import io.ktor.client.features.cookies.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.features.logging.*
import io.ktor.client.features.websocket.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.util.*


@OptIn(InternalAPI::class)
fun httpClientConfig(): HttpClientConfig<*>.()->Unit = {
    Json {
        serializer = KotlinxSerializer(kotlinx.serialization.json.Json {
            ignoreUnknownKeys = true
        })
    }

    Logging {
        level = if (PlatformUtils.IS_JVM)
            LogLevel.ALL
        else
            LogLevel.NONE
    }

    /*HttpResponseValidator {
        validateResponse {
            val result: JsonObject? = it.receive()
            if (result != null){
                if (result["code"]!!.jsonPrimitive.int != ResultState.SUCCEED.code.toInt()){
                    Alerts.msg(result["code"]!!.jsonPrimitive.content)
                }
            }else{
                throw NetException("网络异常")
            }
        }
    }*/

    defaultRequest {
        host = KmBaseUrl.host
        port = KmBaseUrl.port
//        url.protocol = KmBaseUrl.protocol
    }

    install(HttpCookies)

    WebSockets {

    }
}

val KmHttpClient = HttpClient() {

    Json {
        serializer = KotlinxSerializer(kotlinx.serialization.json.Json {
            ignoreUnknownKeys = true
        })
    }

    defaultRequest {
        host = KmBaseUrl.host
        port = KmBaseUrl.port
        url.protocol = KmBaseUrl.protocol
    }

    install(HttpCookies)

    WebSockets {

    }
}


class NetException(message:String) : Exception(message)

object KmBaseUrl{
    const val host = "192.168.50.43"
    const val port = 8081
    val protocol = URLProtocol.HTTPS
    val url = "${protocol.name}://${host}:$port"
}
