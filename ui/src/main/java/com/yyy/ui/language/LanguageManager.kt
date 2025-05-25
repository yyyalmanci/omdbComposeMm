package com.yyy.ui.language

import android.app.LocaleManager
import android.content.Context
import android.os.Build
import androidx.appcompat.app.AppCompatDelegate
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.Locale
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LanguageManager @Inject constructor(
    @ApplicationContext private val context: Context
) {

    fun getCurrentLocaleCode(): String {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            context.getSystemService(LocaleManager::class.java)
                ?.applicationLocales?.toLanguageTags()
                ?: Locale.getDefault().language
        } else {
            AppCompatDelegate.getApplicationLocales().toLanguageTags()
        }
    }

    fun applyLanguage(language: String) {
        if (getCurrentLocaleCode() == language) return

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            context.getSystemService(LocaleManager::class.java).applicationLocales =
                android.os.LocaleList.forLanguageTags(language)
        } else {
            AppCompatDelegate.setApplicationLocales(
                androidx.core.os.LocaleListCompat.forLanguageTags(language)
            )
        }
    }
}