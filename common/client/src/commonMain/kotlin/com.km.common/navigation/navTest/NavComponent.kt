package com.km.common.navigation.navTest

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.*
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.operator.map
import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize
import com.km.common.navigation.counter.CounterRoot
import kotlin.random.Random


class NavComponent(
    componentContext: ComponentContext
): ComponentContext by componentContext {

    private val router: Router<Params, NextComponent> =
        router(
            initialConfiguration = Params(0,"第0"),
            handleBackButton = true,
            childFactory = ::resolveChild
        )

    private fun resolveChild(params: Params, componentContext: ComponentContext) =
        NextComponent(params.id,params.name,componentContext)

    val routerState: Value<RouterState<Params, NextComponent>> = router.state

    val isBackStack = router.state.map {
        it.backStack.isNotEmpty()
    }

    fun next(){
        router.push(Params(Random.nextInt(),"第${router.state.value.backStack.size + 1}"))
    }

    fun prev(){
        router.pop()
    }

    @Parcelize
    data class Params(
        val id:Int,
        val name:String
    ):Parcelable
}