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

@ServerEndpoint("/userWs/{id}")
@Component
class WsServerEndpoint {
    private val logger = Logger.getLogger(WsServerEndpoint::class.java.name)

    companion object {
        private var onlineCount = 0
        private val webSocketMap = ConcurrentHashMap<String,WsServerEndpoint>()

        @Synchronized
        fun incCount() {
            onlineCount++
        }

        @Synchronized
        fun decCount() {
            onlineCount--
        }

        @Synchronized
        fun getOnlineCount() = onlineCount

        @Synchronized
        @Throws(IOException::class)
        fun sendAll(text: String) {
            webSocketMap.forEach { _, u ->
                u.sendMessage(text)
            }
        }

        @Synchronized
        @Throws(IOException::class)
        fun send(id:String,text: String) {
            webSocketMap[id]?.sendMessage(text)
        }
    }

    private lateinit var id:String
    private var session: Session? = null

    @OnOpen
    fun onOpen(session: Session,@PathParam("id") id:String) {
        this.session = session
        incCount()
        webSocketMap[id] = this
        this.id = id

        logger.log(Level.INFO, "onOpen ${session.id} ${toString()} $id")
    }

    @OnClose
    fun onClose(session: Session) {
        this.session = null
        decCount()
        webSocketMap.remove(id)
        logger.log(Level.INFO, "onClose ${session.id}")
    }

    @OnError
    fun onError(session: Session, error: Throwable) {
        this.session = null
        decCount()
        webSocketMap.remove(id)
        logger.log(Level.INFO, "onError ${session.id} $error")
    }

    @OnMessage
    fun onMessage(text: String, session: Session) {
        logger.log(Level.INFO, "onMessage[${session.id}] $text")
    }

    @Throws(IOException::class)
    fun sendMessage(text: String) {
        session?.takeIf { it.isOpen }
            ?.also {
                it.basicRemote.sendText(text)
            }
    }

}