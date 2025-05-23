package com.yyy.domain.usecase

import com.yyy.domain.repository.SettingsRepository
import com.yyy.theme.ThemeOption
import javax.inject.Inject

class SetThemeUseCase @Inject constructor(
    private val settingsRepository: SettingsRepository
) {
    suspend operator fun invoke(theme: ThemeOption) {
        settingsRepository.setTheme(theme)
    }
} 