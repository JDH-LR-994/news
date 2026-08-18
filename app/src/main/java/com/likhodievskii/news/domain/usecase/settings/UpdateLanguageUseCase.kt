package com.likhodievskii.news.domain.usecase.settings

import com.likhodievskii.news.domain.entity.Language
import com.likhodievskii.news.domain.repository.SettingsRepository
import javax.inject.Inject

class UpdateLanguageUseCase @Inject constructor(
    private val settingsRepository: SettingsRepository
) {
    suspend operator fun invoke(language: Language) {
        settingsRepository.updateLanguage(language)
    }
}