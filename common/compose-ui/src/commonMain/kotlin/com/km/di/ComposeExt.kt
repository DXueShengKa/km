package com.km.di

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import org.koin.core.Koin
import org.koin.core.annotation.KoinInternalApi
import org.koin.core.parameter.ParametersDefinition
import org.koin.core.qualifier.Qualifier
import org.koin.core.scope.Scope

expect object GlobalContext{
    actual fun get(): Koin
}

/*
@OptIn(KoinInternalApi::class)
@Composable
inline fun <reified T> koinGet(
    qualifier: Qualifier? = null,
    scope: Scope = GlobalContext.get().scopeRegistry.rootScope,
    noinline parameters: ParametersDefinition? = null,
): T = remember(qualifier, parameters) {
    scope.get(qualifier, parameters)
}

@OptIn(KoinInternalApi::class)
@Composable
inline fun <reified T> inject(
    qualifier: Qualifier? = null,
    scope: Scope = GlobalContext.get().scopeRegistry.rootScope,
    noinline parameters: ParametersDefinition? = null,
): Lazy<T> = remember(qualifier, parameters) {
    scope.inject(qualifier, LazyThreadSafetyMode.NONE, parameters)
}

@Composable
fun getKoin(): Koin = remember {
    GlobalContext.get()
}*/
