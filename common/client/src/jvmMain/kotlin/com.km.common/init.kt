package com.km.common

import com.km.common.api_impl.apiModule
import com.km.common.net.httpClientModule
import org.koin.core.context.startKoin

fun mainInit(){
    startKoin {
        printLogger()
        modules(httpClientModule,apiModule)
    }
}