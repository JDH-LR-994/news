package com.likhodievskii.news.domain.usecase.subscriptions

import com.likhodievskii.news.domain.repository.NewsRepository
import javax.inject.Inject

data class ClearAllArticlesUseCase @Inject constructor(
    private val newsRepository: NewsRepository
) {
    suspend operator fun invoke(topics: List<String>) {
        newsRepository.clearAllArticles(topics)
    }
}