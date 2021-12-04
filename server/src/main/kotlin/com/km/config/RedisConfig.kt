package com.km.config

import org.springframework.cache.annotation.CachingConfigurerSupport
import org.springframework.cache.annotation.EnableCaching
import org.springframework.cache.interceptor.KeyGenerator
import org.springframework.context.annotation.Configuration

@Configuration
@EnableCaching
class RedisConfig : CachingConfigurerSupport() {

    override fun keyGenerator(): KeyGenerator {
        return KeyGenerator { target, method, params ->
            return@KeyGenerator buildString {
                append(target::class.java.simpleName)
                append('.')
                append(method.name)
                append(':')
                params.joinToString(
                    prefix = "[",
                    postfix = "]"
                ) { it.toString() }
            }
        }
    }
}