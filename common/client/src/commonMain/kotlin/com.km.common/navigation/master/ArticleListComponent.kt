package com.km.common.navigation.master

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.reduce


import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

internal class ArticleListComponent(
    componentContext: ComponentContext,
    database: ArticleDatabase,
    selectedArticleId: Flow<Long?>,
    private val onArticleSelected: (id: Long) -> Unit
) : ArticleList, ComponentContext by componentContext, CoroutineScope by componentContext.disposableScope() {

    private val _models =
        MutableValue(
            ArticleList.Model(
                articles = database.getAll().map { it.toArticle() },
                selectedArticleId = null
            )
        )

    override val models: Value<ArticleList.Model> = _models

    init {
        launch {
            selectedArticleId.collect { id ->
                _models.reduce { it.copy(selectedArticleId = id) }
            }
        }
    }

    private fun ArticleEntity.toArticle(): ArticleList.Article =
        ArticleList.Article(
            id = id,
            title = title
        )

    override fun onArticleClicked(id: Long) {
        onArticleSelected(id)
    }
}
