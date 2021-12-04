package com.km.common.navigation.master
import com.arkivanov.decompose.value.Value

interface ArticleDetails {

    val models: Value<Model>

    fun onCloseClicked()

    data class Model(
        val isToolbarVisible: Boolean,
        val article: Article
    )

    data class Article(
        val title: String,
        val text: String
    )
}
