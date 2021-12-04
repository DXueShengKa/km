package com.km.common.navigation.master

import com.arkivanov.decompose.router.RouterState
import com.arkivanov.decompose.value.Value

interface Root {

    val models: Value<Model>
    val listRouterState: Value<RouterState<*, ListChild>>
    val detailsRouterState: Value<RouterState<*, DetailsChild>>

    fun setMultiPane(isMultiPane: Boolean)

    data class Model(
        val isMultiPane: Boolean = false
    )

    sealed class ListChild {
        class List(val component: ArticleList) : ListChild()
        object None : ListChild()
    }

    sealed class DetailsChild {
        object None : DetailsChild()
        data class Details(val component: ArticleDetails) : DetailsChild()
    }
}
