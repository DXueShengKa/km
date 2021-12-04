package com.km.common.viewmodel

import androidx.compose.runtime.*
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.ValueObserver
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.arkivanov.essenty.lifecycle.create
import com.arkivanov.essenty.lifecycle.destroy

@Composable
fun rememberComponentContext(
    componentContextFactory:(ComponentContext)->ComponentContext
): ComponentContext {
    return remember { DecomposeComponent(componentContextFactory) }.componentContext
}

@Composable
fun <T : Any> Value<T>.subscribeAsState(): State<T> {
    val state = remember(this) { mutableStateOf(value) }

    DisposableEffect(this) {
        val observer: ValueObserver<T> = { state.value = it }

        subscribe(observer)

        onDispose {
            unsubscribe(observer)
        }
    }

    return state
}


class DecomposeComponent(
    componentContextFactory:(ComponentContext)->ComponentContext
): RememberObserver {
    private val lifecycle =  LifecycleRegistry()
    private val ctx = DefaultComponentContext(lifecycle)

    val componentContext = componentContextFactory(ctx)

    override fun onAbandoned() {

    }
    override fun onRemembered() {
        lifecycle.create()
    }

    override fun onForgotten() {
        lifecycle.destroy()
    }


}