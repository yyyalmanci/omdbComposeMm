package com.yyy.domain.repository

import com.yyy.theme.ThemeOption
import kotlinx.coroutines.flow.Flow

interface SettingsRepository {
    val theme: Flow<ThemeOption>
    suspend fun setTheme(theme: ThemeOption)
} 