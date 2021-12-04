package com.km.common.navigation.counter

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.childContext
import com.arkivanov.decompose.router.*
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize


class CounterRootComponent(
    componentContext: ComponentContext
) : CounterRoot, ComponentContext by componentContext {

    override val counter: Counter = CounterComponent(childContext(key = "counter"), index = 0)

    private val router: Router<ChildConfiguration, CounterRoot.Child> =
        router(
            initialConfiguration = ChildConfiguration(index = 0, isBackEnabled = false),
            handleBackButton = true,
            childFactory = ::resolveChild
        )

    override val routerState: Value<RouterState<*, CounterRoot.Child>> = router.state

    private fun resolveChild(configuration: ChildConfiguration, componentContext: ComponentContext) =
        CounterRoot. Child(
            inner = CounterInnerComponent(componentContext, index = configuration.index),
            isBackEnabled = configuration.isBackEnabled
        )

    override fun onNextChild() {
        router.push(ChildConfiguration(index = router.state.value.backStack.size + 1, isBackEnabled = true))
    }

    override fun onPrevChild() {
        router.pop()
    }

    @Parcelize
    private data class ChildConfiguration(val index: Int, val isBackEnabled: Boolean) : Parcelable
}
