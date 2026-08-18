package com.likhodievskii.news.domain.usecase.settings

import com.likhodievskii.news.domain.entity.Interval
import com.likhodievskii.news.domain.repository.SettingsRepository
import javax.inject.Inject

class UpdateIntervalUseCase @Inject constructor(
    private val settingsRepository: SettingsRepository
) {
    suspend operator fun invoke(interval: Interval) {
        settingsRepository.updateInterval(interval.minutes)
    }
}