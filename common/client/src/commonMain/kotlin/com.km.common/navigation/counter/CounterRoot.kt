package com.km.common.navigation.counter

import com.arkivanov.decompose.router.RouterState
import com.arkivanov.decompose.value.Value



interface CounterRoot {

    val counter: Counter
    val routerState: Value<RouterState<*, Child>>

    fun onNextChild()
    fun onPrevChild()

    class Child(
        val inner: CounterInner,
        val isBackEnabled: Boolean
    )
}