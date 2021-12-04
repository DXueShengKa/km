package com.km.common

import android.app.Application
import com.km.common.api_impl.apiModule
import com.km.common.net.httpClientModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

open class CommonApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@CommonApplication)
            modules(httpClientModule,apiModule)
        }
    }
}
