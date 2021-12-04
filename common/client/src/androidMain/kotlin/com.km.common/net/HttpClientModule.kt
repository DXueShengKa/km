package com.km.common.net

import android.content.Context
import io.ktor.client.*
import io.ktor.client.engine.okhttp.*
import org.koin.dsl.module
import java.security.KeyStore
import java.security.cert.CertificateFactory
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManagerFactory
import javax.net.ssl.X509TrustManager

val httpClientModule = module {
    factory { httpClientConfig() }
    single {
        HttpClient(OkHttp) {
            engine {
                ssl(get())
            }
            apply(get<HttpClientConfig<*>.() -> Unit>())
        }
    }
}

private fun OkHttpConfig.ssl(content: Context) {
    val keyStore = KeyStore.getInstance(KeyStore.getDefaultType())
    keyStore.load(null)

    val certificateFactory = CertificateFactory.getInstance("X.509")
    keyStore.setCertificateEntry(
        "serverkey", certificateFactory.generateCertificate(
            content.assets.open("km.cer")
        )
    )

    val trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm())
    trustManagerFactory.init(keyStore)

    val trustManagers = trustManagerFactory.trustManagers

    if (trustManagers.size != 1 || trustManagers[0] !is X509TrustManager) {
        throw IllegalStateException("这不是默认的信任管理器${trustManagers}")
    }

    val sslContext = SSLContext.getInstance("TLS")
    sslContext.init(null, trustManagers, null)

    this.config {
        sslSocketFactory(sslContext.socketFactory, trustManagers[0] as X509TrustManager)
    }

}