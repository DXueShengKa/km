package com.km.common.navigation.user

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.km.common.api.IUserApi
import com.km.common.entity.UserInfo
import com.km.common.navigation.componentScope
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.get

class UserListComponent(
    componentContext: ComponentContext,
    private val onUserInfo: (Int) -> Unit
) : UserRootComponent.RC, KoinComponent, ComponentContext by componentContext {

    private val userApi:IUserApi = get()

    /*    private val router: Router<OneData<Int?>, RouterChild> =
            router(
                initialConfiguration = OneData(null),
                handleBackButton = true,
                childFactory = { d ,ctx->
                    if (d.data == null) {
                        RouterChild.None
                    }else
                        RouterChild.Next(UserInfoComponent(d.data,ctx))
                }
            )

        val infoRouter:Value<RouterState<OneData<Int?>,RouterChild>> = router.state

        fun toUserInfo(id:Int){
            router.push(OneData(id))
        }*/
    init {
        getData()
    }

    fun toUserInfo(id: Int) {
        onUserInfo(id)
    }

    val users = MutableValue(listOf<UserInfo>())

    fun getData() {
        componentScope.launch {

            val d = userApi.list().succeedData
            users.value = d
//            UserApi.list().takeIf {
//                if (! it.isSucceed) Alerts.msg(it.message)
//                it.isSucceed
//            }?.also {
//                users.value = it.succeedData
//            }
        }
    }

}