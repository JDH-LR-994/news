package com.likhodievskii.news.domain.usecase.subscriptions

import com.likhodievskii.news.domain.repository.NewsRepository
import javax.inject.Inject

data class RemoveSubscriptionUseCase @Inject constructor(
    private val newsRepository: NewsRepository
) {
    suspend operator fun invoke(topic: String) {
        newsRepository.removeSubscription(topic)
    }
}