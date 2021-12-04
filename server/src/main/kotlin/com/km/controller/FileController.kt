package com.km.controller

import com.km.utils.VideoStream
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import java.io.IOException
import java.io.OutputStream
import java.net.URLDecoder
import java.nio.ByteBuffer
import java.nio.channels.ByteChannel
import java.nio.file.Files
import java.nio.file.Paths
import java.util.logging.Level
import java.util.logging.Logger
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse


@Controller
@RequestMapping("/file")
class FileController {

    private val logger = Logger.getLogger(FileController::class.java.name)

    @GetMapping("/getVideo")
    fun getVideo(response: HttpServletResponse) {
        VideoStream.getVideo(response)
    }

    @GetMapping("/player")
    fun player(request: HttpServletRequest, response: HttpServletResponse) {
        VideoStream.player(request, response, logger)
    }

    @GetMapping("/image")
    fun image(url: String?, response: HttpServletResponse) {
        if (url == null){
            logger.log(Level.INFO,"没有路径")
            response.sendError(HttpServletResponse.SC_NOT_FOUND)
            return
        }
        val outputStream = response.outputStream
        try {
            val path = Paths.get(URLDecoder.decode(url,Charsets.UTF_8))
            logger.log(Level.INFO,url)
            response.setContentLengthLong(Files.size(path))
            copyTo(Files.newByteChannel(path),outputStream)
        }catch (e:IOException){
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR)
            logger.log(Level.SEVERE,e.stackTraceToString())
        }finally {
            outputStream.close()
        }
    }

    @Throws(IOException::class)
    private fun copyTo(inChannel: ByteChannel, out: OutputStream){
        val byteBuffer = ByteBuffer.allocate(4096)
        var len:Int
        while (inChannel.read(byteBuffer).also { len = it } != -1){
            byteBuffer.flip()
            out.write(byteBuffer.array(),0,len)
            byteBuffer.clear()
        }
        inChannel.close()
    }

}