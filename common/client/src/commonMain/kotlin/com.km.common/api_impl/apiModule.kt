package com.km.common.api_impl

import com.km.common.api.ILoginApi
import com.km.common.api.IUserApi
import org.koin.dsl.module

val apiModule = module {
    single<IUserApi> { UserApiImpl(get()) }
    single<ILoginApi> { LoginApiImpl(get()) }

}