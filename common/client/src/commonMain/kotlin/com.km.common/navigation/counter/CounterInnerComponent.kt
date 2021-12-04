package com.km.common.navigation.counter

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.childContext
import com.arkivanov.decompose.router.*
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize

class CounterInnerComponent(
    componentContext: ComponentContext,
    index: Int
) : CounterInner, ComponentContext by componentContext {


    override val counter: Counter = CounterComponent(childContext(key = "counter"), index = index)

    private val leftRouter: Router<ChildConfiguration, CounterInner.Child> =
        router(
            initialConfiguration = ChildConfiguration(index = 0, isBackEnabled = false),
            key = "LeftRouter",
            childFactory = ::resolveChild
        )

    override val leftRouterState: Value<RouterState<*, CounterInner.Child>> = leftRouter.state

    private val rightRouter: Router<ChildConfiguration, CounterInner.Child> =
        router(
            initialConfiguration = ChildConfiguration(index = 0, isBackEnabled = false),
            key = "RightRouter",
            childFactory = ::resolveChild
        )

    override val rightRouterState: Value<RouterState<*, CounterInner.Child>> = rightRouter.state

    private fun resolveChild(configuration: ChildConfiguration, componentContext: ComponentContext): CounterInner.Child =
        CounterInner.Child(
            counter = CounterComponent(componentContext, index = configuration.index),
            isBackEnabled = configuration.isBackEnabled
        )

    override fun onNextLeftChild() {
        leftRouter.pushNextChild()
    }

    override fun onPrevLeftChild() {
        leftRouter.pop()
    }

    override fun onNextRightChild() {
        rightRouter.pushNextChild()
    }

    override fun onPrevRightChild() {
        rightRouter.pop()
    }

    private fun Router<ChildConfiguration, *>.pushNextChild() {
        push(ChildConfiguration(index = state.value.backStack.size + 1, isBackEnabled = true))
    }

    @Parcelize
    private data class ChildConfiguration(val index: Int, val isBackEnabled: Boolean) : Parcelable
}
