package com.km.common.api_impl


import com.km.common.api.IUserApi
import com.km.common.api.IUserApi.Companion.list
import com.km.common.api.IUserApi.Companion.user
import com.km.common.entity.UserInfo
import com.km.common.net.NetResult
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.http.*
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonPrimitive


internal class UserApiImpl(private val httpClient: HttpClient) : IUserApi {

    override suspend fun list(): NetResult<List<UserInfo>> {
        return httpClient.get("$user$list")
    }

    @OptIn(ExperimentalSerializationApi::class)
    override suspend fun save(userInfo: UserInfo): NetResult<UserInfo> {

        return httpClient.submitForm(user, Parameters.build {

            val jo: JsonObject = Json.decodeFromString(
                Json.encodeToString(userInfo)
            )

            jo.entries.forEach {
                append(it.key, it.value.jsonPrimitive.content)
            }

        })
    }

    override suspend fun get(id: Int): NetResult<UserInfo> {
        return httpClient.get("$user/$id")
    }

}