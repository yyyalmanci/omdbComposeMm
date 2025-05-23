package com.yyy.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yyy.domain.usecase.SetThemeUseCase
import com.yyy.theme.ThemeOption
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val setThemeUseCase: SetThemeUseCase
) : ViewModel() {


    fun setTheme(theme: ThemeOption) {
        viewModelScope.launch {
            setThemeUseCase(theme)
        }
    }
} 