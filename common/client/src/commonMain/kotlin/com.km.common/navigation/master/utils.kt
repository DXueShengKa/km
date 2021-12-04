package com.km.common.navigation.master

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.lifecycle.doOnDestroy
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import kotlin.coroutines.EmptyCoroutineContext


internal fun ComponentContext.disposableScope(): CoroutineScope {
    val scope = CoroutineScope(EmptyCoroutineContext)
    lifecycle.doOnDestroy(scope::cancel)
    return scope
}
