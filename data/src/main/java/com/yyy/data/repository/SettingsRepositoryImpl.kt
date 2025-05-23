package com.yyy.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.yyy.domain.repository.SettingsRepository
import com.yyy.theme.ThemeOption
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

@Singleton
class SettingsRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : SettingsRepository {

    private object PreferencesKeys {
        val THEME = stringPreferencesKey("theme")
    }

    override val theme: Flow<ThemeOption> = context.dataStore.data
        .map { preferences ->
            preferences[PreferencesKeys.THEME]?.let { ThemeOption.valueOf(it) }
                ?: ThemeOption.SYSTEM
        }

    override suspend fun setTheme(theme: ThemeOption) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.THEME] = theme.name
        }
    }
} 