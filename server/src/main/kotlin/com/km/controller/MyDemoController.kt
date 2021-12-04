package com.km.controller

import com.km.websocket.WsServerEndpoint
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.time.Duration

@RestController
class MyDemoController {

    @Autowired
    private lateinit var srt: StringRedisTemplate

    @GetMapping(
        value = ["/myDemo/price/{name}"],
        produces = [MediaType.TEXT_EVENT_STREAM_VALUE]
    )
    fun price(@PathVariable name: String): Flux<String> {
        return Flux.interval(Duration.ofSeconds(2L)).map {
            buildJsonObject {
                put("name", name)
                put("RedisV", srt.opsForValue().get("RedisV") ?: "null")
                put("time", java.time.LocalDateTime.now().toString())
            }.toString()
        }
    }

    @GetMapping(
        value = ["/myDemo/price2/{txt}"]
    )
    fun price2(@PathVariable txt: String): Mono<String> {
        WsServerEndpoint.sendAll(txt)
        return Mono.just("发送成功")
    }


    @GetMapping(
        value = ["/myDemo/setRedisV"]
    )
    fun setRedisV(v: String): String {
        srt.opsForValue().set("RedisV", v, Duration.ofSeconds(6L))
        return "已更新"
    }
}