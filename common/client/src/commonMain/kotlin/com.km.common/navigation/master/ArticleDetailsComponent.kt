package com.km.common.navigation.master

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.reduce

import kotlinx.coroutines.CoroutineScope

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch


internal class ArticleDetailsComponent(
    componentContext: ComponentContext,
    database: ArticleDatabase,
    articleId: Long,
    isToolbarVisible: Flow<Boolean>,
    private val onFinished: () -> Unit
) : ArticleDetails, ComponentContext by componentContext, CoroutineScope by componentContext.disposableScope() {

    private val _models =
        MutableValue(
            ArticleDetails.Model(
                isToolbarVisible = false,
                article = database.getById(id = articleId).toArticle()
            )
        )

    override val models: Value<ArticleDetails.Model> = _models

    init {
        launch {
            isToolbarVisible.collect { isVisible ->
                _models.reduce { it.copy(isToolbarVisible = isVisible) }
            }
        }
    }

    private fun ArticleEntity.toArticle(): ArticleDetails.Article =
        ArticleDetails.Article(
            title = title,
            text = text
        )

    override fun onCloseClicked() {
        onFinished()
    }
}
