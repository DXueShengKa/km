package com.km.common.api_impl

import com.km.common.net.KmBaseUrl
import com.km.common.net.KmHttpClient
import io.ktor.client.features.websocket.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.http.cio.websocket.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class UserWs(id:String) {
    private val sendChannel = Channel<String>()

    val flow = flow<String> {
        KmHttpClient.wss(method = HttpMethod.Get, host = KmBaseUrl.host, port = KmBaseUrl.port, path = "userWs/$id") {
            launch {
                while (isActive) {
                    send(
                        sendChannel.receive()
                    )
                }
            }

            while (isActive) {
                val othersMessage = incoming.receive() as? Frame.Text
                othersMessage?.readText()?.also {
                    emit(it)
                }
            }
        }
    }

    fun send(text: String) {
        sendChannel.trySend(text)
    }

    suspend fun sendAll(text: String):String{
        return KmHttpClient.get("/myDemo/price2/$text")
    }

}