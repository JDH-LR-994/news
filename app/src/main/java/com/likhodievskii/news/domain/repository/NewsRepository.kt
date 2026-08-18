package com.likhodievskii.news.domain.repository

import com.likhodievskii.news.domain.entity.Article
import com.likhodievskii.news.domain.entity.Language
import com.likhodievskii.news.domain.entity.RefreshConfig
import kotlinx.coroutines.flow.Flow

interface NewsRepository {
    fun getAllSubscription(): Flow<List<String>>

    fun startBackgroundRefresh(refreshConfig: RefreshConfig)

    suspend fun addSubscription(topic: String)

    suspend fun updateArticlesForTopic(topic: String, language: Language) : Boolean

    suspend fun removeSubscription(topic: String)

    suspend fun updateArticlesForAllSubscriptions(language: Language) : List<String>

    fun getArticlesByTopics(topics: List<String>) : Flow<List<Article>>

    suspend fun clearAllArticles(topics: List<String>)
}