package com.km.common.navigation.master

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.RouterState
import com.arkivanov.decompose.router.pop
import com.arkivanov.decompose.router.push
import com.arkivanov.decompose.router.router
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize

import kotlinx.coroutines.flow.Flow

internal class ListRouter(
    componentContext: ComponentContext,
    private val database: ArticleDatabase,
    private val selectedArticleId: Flow<Long?>,
    private val onArticleSelected: (id: Long) -> Unit
) {

    private val router =
        componentContext.router<Config, Root.ListChild>(
            initialConfiguration = Config.List,
            key = "MainRouter",
            childFactory = ::createChild
        )

    val state: Value<RouterState<Config, Root.ListChild>> = router.state

    private fun createChild(config: Config, componentContext: ComponentContext): Root.ListChild =
        when (config) {
            is Config.List -> Root.ListChild.List(articleList(componentContext))
            is Config.None -> Root.ListChild.None
        }

    private fun articleList(componentContext: ComponentContext): ArticleList =
        ArticleListComponent(
            componentContext = componentContext,
            database = database,
            selectedArticleId = selectedArticleId,
            onArticleSelected = onArticleSelected
        )

    fun moveToBackStack() {
        if (router.state.value.activeChild.configuration !is Config.None) {
            router.push(Config.None)
        }
    }

    fun show() {
        if (router.state.value.activeChild.configuration !is Config.List) {
            router.pop()
        }
    }

    sealed interface Config : Parcelable {
        @Parcelize
        object List : Config

        @Parcelize
        object None : Config
    }
}
