package com.km.common.net

import io.ktor.client.*
import io.ktor.client.engine.java.*
import org.koin.dsl.module
import java.security.KeyStore
import java.security.cert.CertificateFactory
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManagerFactory

val httpClientModule = module {
    factory { httpClientConfig() }

    single {
        HttpClient(Java) {
            engine {
                ssl()
            }
            apply(get<HttpClientConfig<*>.() -> Unit>())
        }
    }
}

private fun JavaHttpConfig.ssl() {

    val keyStore = KeyStore.getInstance(KeyStore.getDefaultType())
    keyStore.load(null)

    val certificateFactory = CertificateFactory.getInstance("X.509")
    keyStore.setCertificateEntry(
        "serverkey", certificateFactory.generateCertificate(
            javaClass.classLoader.getResourceAsStream("km.cer")
        )
    )

    val trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm())
    trustManagerFactory.init(keyStore)

    val sslContext = SSLContext.getInstance("TLS")
    sslContext.init(null, trustManagerFactory.trustManagers, null)

    this.config {
        sslContext(sslContext)
    }
}