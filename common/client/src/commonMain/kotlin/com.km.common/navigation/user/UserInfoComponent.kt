package com.km.common.navigation.user

import com.arkivanov.decompose.ComponentContext
import com.km.common.api.IUserApi
import com.km.common.entity.UserInfo
import com.km.common.navigation.componentScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.get

class UserInfoComponent(
    private val userId: Int,
    componentContext: ComponentContext,
    val onBack:()->Unit
) :UserRootComponent.RC, KoinComponent,ComponentContext by componentContext {
    private val userApi: IUserApi = get()

    val user = MutableStateFlow<UserInfo?>(null)

    init {
        componentScope.launch {
            user.value = userApi.get(userId).succeedData
        }
    }


}