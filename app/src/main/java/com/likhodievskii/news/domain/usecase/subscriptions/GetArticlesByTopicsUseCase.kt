package com.likhodievskii.news.domain.usecase.subscriptions

import com.likhodievskii.news.domain.repository.NewsRepository
import javax.inject.Inject

data class GetArticlesByTopicsUseCase @Inject constructor(
    private val newsRepository: NewsRepository
) {
    operator fun invoke(topics: List<String>) =
        newsRepository.getArticlesByTopics(topics)

}