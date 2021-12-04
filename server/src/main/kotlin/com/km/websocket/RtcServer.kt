package com.km.websocket

import org.springframework.stereotype.Component
import java.io.IOException
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentMap
import java.util.concurrent.CopyOnWriteArraySet
import java.util.logging.Level
import java.util.logging.Logger
import javax.websocket.*
import javax.websocket.server.PathParam
import javax.websocket.server.ServerEndpoint
import kotlin.jvm.Throws

@ServerEndpoint("/rtc")
@Component
class RtcServer {
    private val logger = Logger.getLogger(RtcServer::class.java.name)

    companion object {
        private val webSocketSet = CopyOnWriteArraySet<RtcServer>()

        @Synchronized
        @Throws(IOException::class)
        fun sendAll(text: String) {
            webSocketSet.forEach { u ->
                u.sendMessage(text)
            }
        }

    }

    private var session: Session? = null

    @OnOpen
    fun onOpen(session: Session) {
        this.session = session
        webSocketSet.add(this)
        logger.log(Level.INFO, "onOpen ${session.id}")
    }

    @OnClose
    fun onClose(session: Session) {
        this.session = null
        webSocketSet.remove(this)
        logger.log(Level.INFO, "onClose ${session.id}")
    }

    @OnError
    fun onError(session: Session, error: Throwable) {
        this.session = null
        logger.log(Level.INFO, "onError ${session.id} $error")
    }

    @OnMessage
    fun onMessage(text: String, session: Session) {
        logger.log(Level.INFO, "onMessage[${session.id}] $text")
        for (r in webSocketSet){
            if (r == this) continue
            r.sendMessage(text)
        }
    }

    @Throws(IOException::class)
    fun sendMessage(text: String) {
        session?.takeIf { it.isOpen }
            ?.also {
                it.basicRemote.sendText(text)
            }
    }

}