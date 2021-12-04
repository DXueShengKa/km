package com.km.common.navigation.counter

import com.arkivanov.decompose.router.RouterState
import com.arkivanov.decompose.value.Value

interface CounterInner {

    val counter: Counter
    val leftRouterState: Value<RouterState<*, Child>>
    val rightRouterState: Value<RouterState<*, Child>>

    fun onNextLeftChild()
    fun onPrevLeftChild()
    fun onNextRightChild()
    fun onPrevRightChild()

    class Child(
        val counter: Counter,
        val isBackEnabled: Boolean
    )
}
