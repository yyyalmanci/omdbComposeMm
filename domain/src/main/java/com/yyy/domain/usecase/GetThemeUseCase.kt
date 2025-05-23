package com.yyy.domain.usecase

import com.yyy.domain.repository.SettingsRepository
import com.yyy.theme.ThemeOption
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetThemeUseCase @Inject constructor(
    private val settingsRepository: SettingsRepository
) {
    operator fun invoke(): Flow<ThemeOption> = settingsRepository.theme
} 