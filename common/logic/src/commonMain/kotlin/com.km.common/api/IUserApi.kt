package com.km.common.api

import com.km.common.entity.UserInfo
import com.km.common.net.NetResult

interface IUserApi {
    suspend fun list(): NetResult<List<UserInfo>>
    suspend fun save(userInfo: UserInfo): NetResult<UserInfo>
    suspend fun get(id:Int):NetResult<UserInfo>

    companion object{
        const val user = "/user"
        const val list = "/list"
    }
}