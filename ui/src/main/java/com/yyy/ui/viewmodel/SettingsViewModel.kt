package com.yyy.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yyy.domain.usecase.GetLanguageUseCase
import com.yyy.domain.usecase.SetLanguageUseCase
import com.yyy.domain.usecase.SetThemeUseCase
import com.yyy.theme.LangOption
import com.yyy.theme.ThemeOption
import com.yyy.ui.language.LanguageManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val setThemeUseCase: SetThemeUseCase,
    private val setLanguageUseCase: SetLanguageUseCase,
    private val getLanguageUseCase: GetLanguageUseCase,
    private val languageManager: LanguageManager
) : ViewModel() {

    private val _langState = MutableStateFlow("")
    val langState: StateFlow<String> = _langState.asStateFlow()

    init {
        viewModelScope.launch {
            getLanguageUseCase().collect { lang ->
                _langState.update {
                    lang
                }
            }
        }
    }

    fun setTheme(theme: ThemeOption) {
        viewModelScope.launch {
            setThemeUseCase(theme)
        }
    }

    fun setLanguage(language: LangOption) {
        viewModelScope.launch {
            setLanguageUseCase(language.code)
            languageManager.applyLanguage(language.code)
        }
    }
} 