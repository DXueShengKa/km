package com.km

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration
import org.springframework.boot.runApplication


@SpringBootApplication(
    exclude = [
        SecurityAutoConfiguration::class
    ]
)
class KmApplication


fun main(args: Array<String>) {
    runApplication<KmApplication>(*args)
}

