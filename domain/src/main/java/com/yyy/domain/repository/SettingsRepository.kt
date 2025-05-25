package com.yyy.domain.repository

import com.yyy.theme.ThemeOption
import kotlinx.coroutines.flow.Flow

interface SettingsRepository {
    val theme: Flow<ThemeOption>
    val language: Flow<String>
    suspend fun setTheme(theme: ThemeOption)
    suspend fun setLanguage(language: String)
} 