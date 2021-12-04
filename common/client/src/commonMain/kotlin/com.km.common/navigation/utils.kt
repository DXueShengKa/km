package com.km.common.navigation

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.instancekeeper.InstanceKeeper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlin.coroutines.CoroutineContext


val ComponentContext.componentScope: CoroutineScope
    get() = instanceKeeper.get(COMPONENT_SCOPE) as? ComponentScope
        ?: ComponentScope().also {
            instanceKeeper.put(COMPONENT_SCOPE, it)
        }

private const val COMPONENT_SCOPE = "COMPONENT_SCOPE"

class ComponentScope(
    context: CoroutineContext = SupervisorJob() + Dispatchers.Main.immediate,
) : InstanceKeeper.Instance, CoroutineScope by CoroutineScope(context) {
    override fun onDestroy() {
        cancel()
    }
}