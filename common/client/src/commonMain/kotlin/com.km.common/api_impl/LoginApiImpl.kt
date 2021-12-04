package com.km.common.api_impl


import com.km.common.api.ILoginApi
import com.km.common.api.ILoginApi.Companion.login
import com.km.common.api.ILoginApi.Companion.logout
import com.km.common.entity.UserInfo
import com.km.common.net.NetResult
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.http.*


internal class LoginApiImpl(private val httpClient: HttpClient) : ILoginApi {

    override suspend fun loginInfo(): NetResult<UserInfo> {

        return httpClient.get(login)
    }

    override suspend fun logout(): NetResult<String> {
        return httpClient.get("$login$logout")
    }

    override suspend fun login(username: String, password: String): NetResult<UserInfo> {

        return httpClient.submitForm(login, Parameters.build {
            append("username", username)
            append("password", password)
        })
    }


}