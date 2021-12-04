package com.km.common.navigation.master

import com.arkivanov.decompose.value.Value

interface ArticleList {

    val models: Value<Model>

    fun onArticleClicked(id: Long)

    data class Model(
        val articles: List<Article>,
        val selectedArticleId: Long?
    )

    data class Article(
        val id: Long,
        val title: String
    )
}
