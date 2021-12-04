package com.km.common.navigation.master

import com.km.common.navigation.master.LorenIpsumGenerator.generate
import com.km.common.navigation.master.LorenIpsumGenerator.generateSentence
import kotlin.random.Random

internal interface ArticleDatabase {

    fun getAll(): List<ArticleEntity>

    fun getById(id: Long): ArticleEntity
}

internal class DefaultArticleDatabase : ArticleDatabase {

    private val articles =
        List(50) { index ->
            ArticleEntity(
                id = index.toLong() + 1L,
                title = generate(count = Random.nextInt(3, 7), minWordLength = 3)
                    .joinToString(separator = " ", transform = String::capitalize),
                text = List(50) { generateSentence() }
                    .joinToString(separator = " ")
            )
        }

    private val articleMap = articles.associateBy(ArticleEntity::id)

    override fun getAll(): List<ArticleEntity> = articles

    override fun getById(id: Long): ArticleEntity = articleMap.getValue(id)
}
