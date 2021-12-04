package com.km.common.navigation.user

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.*
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize

class UserRootComponent(
    componentContext: ComponentContext,
) : ComponentContext by componentContext {

    private val _router: Router<OneData, RC> =
        router(
            initialConfiguration = OneData.List,
            handleBackButton = true,
            childFactory = ::childFactory
        )

    val router: Value<RouterState<OneData, RC>>
            = _router.state

    private fun childFactory(d:OneData,ctx:ComponentContext):RC{
        return when(d){
                is OneData.List -> UserListComponent(ctx){
                    _router.push(OneData.Info(it))
                }
                is OneData.Info -> UserInfoComponent(d.id,ctx){
                    _router.pop()
                }
            }
    }


    sealed interface OneData: Parcelable{
        @Parcelize
        object List:OneData

        @Parcelize
        class Info(val id:Int):OneData
    }

    sealed interface RC
}