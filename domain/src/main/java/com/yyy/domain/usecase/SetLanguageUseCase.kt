package com.yyy.domain.usecase

import com.yyy.domain.repository.SettingsRepository
import javax.inject.Inject

class SetLanguageUseCase @Inject constructor(
    private val settingsRepository: SettingsRepository
) {
    suspend operator fun invoke(language: String) {
        settingsRepository.setLanguage(language)
    }
} 