package com.km.common.net


import io.ktor.client.*
import io.ktor.client.engine.*
import org.koin.dsl.module

val httpClientModule = module {
    factory { httpClientConfig() }
    single {
        HttpClient(JsEngineFactory) {
            engine {

            }
            apply(get<HttpClientConfig<*>.() -> Unit>())
        }
    }
}

private val JsEngineFactory = object : HttpClientEngineFactory<HttpClientEngineConfig> {
    override fun create(block: HttpClientEngineConfig.() -> Unit) =
        JsClientEngine(HttpClientEngineConfig().apply(block))
}
