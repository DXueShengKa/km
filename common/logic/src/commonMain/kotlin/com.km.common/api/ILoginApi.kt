package com.km.common.api

import com.km.common.entity.UserInfo
import com.km.common.net.NetResult

interface ILoginApi {

    suspend fun loginInfo(): NetResult<UserInfo>
    suspend fun logout(): NetResult<String>
    suspend fun login(username: String, password: String): NetResult<UserInfo>

    companion object{
        const val login = "login"
        const val logout = "logout"
    }
}