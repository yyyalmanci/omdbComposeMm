package com.yyy.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.yyy.theme.LangOption
import com.yyy.theme.ThemeOption
import com.yyy.ui.R
import com.yyy.ui.viewmodel.SettingsViewModel

@Composable
fun SettingsScreen(
    theme: ThemeOption,
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val currentLanguage by viewModel.langState.collectAsState()

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // Theme Settings
            Text(
                text = stringResource(R.string.theme_settings),
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            ) {
                ThemeOptionSelector(
                    currentTheme = theme,
                    onThemeSelected = viewModel::setTheme
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Language Settings
            Text(
                text = stringResource(R.string.language_settings),
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            ) {
                LanguageOptionSelector(
                    currentLanguage = LangOption.fromCode(currentLanguage),
                    onLanguageSelected = viewModel::setLanguage
                )
            }
        }
    }
}

@Composable
private fun ThemeOptionSelector(
    currentTheme: ThemeOption,
    onThemeSelected: (ThemeOption) -> Unit
) {
    Column(
        modifier = Modifier.padding(16.dp)
    ) {
        ThemeOption.entries.forEach { themeOption ->
            val isSelected = currentTheme == themeOption
            val themeText = when (themeOption) {
                ThemeOption.LIGHT -> stringResource(R.string.light_theme)
                ThemeOption.DARK -> stringResource(R.string.dark_theme)
                ThemeOption.SYSTEM -> stringResource(R.string.system_theme)
            }

            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                color = if (isSelected) {
                    MaterialTheme.colorScheme.primaryContainer
                } else {
                    MaterialTheme.colorScheme.surface
                },
                shape = MaterialTheme.shapes.medium,
                onClick = { onThemeSelected(themeOption) }
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp)
                        .semantics {
                            role = Role.RadioButton
                            contentDescription = themeText
                        },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = isSelected,
                        onClick = null,
                        colors = RadioButtonDefaults.colors(
                            selectedColor = MaterialTheme.colorScheme.primary,
                            unselectedColor = MaterialTheme.colorScheme.onSurfaceVariant
                        ),
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = themeText,
                        style = MaterialTheme.typography.bodyLarge,
                        color = if (isSelected) {
                            MaterialTheme.colorScheme.onPrimaryContainer
                        } else {
                            MaterialTheme.colorScheme.onSurface
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun LanguageOptionSelector(
    currentLanguage: LangOption,
    onLanguageSelected: (LangOption) -> Unit
) {
    Column(
        modifier = Modifier.padding(16.dp)
    ) {
        listOf(LangOption.TR, LangOption.EN).forEach { lang ->
            val isSelected = currentLanguage == lang
            val languageText = when (lang) {
                LangOption.TR -> stringResource(R.string.turkish)
                LangOption.EN -> stringResource(R.string.english)
                else -> {
                    return
                }
            }

            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                color = if (isSelected) {
                    MaterialTheme.colorScheme.primaryContainer
                } else {
                    MaterialTheme.colorScheme.surface
                },
                shape = MaterialTheme.shapes.medium,
                onClick = { onLanguageSelected(lang) }
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp)
                        .semantics {
                            role = Role.RadioButton
                            contentDescription = languageText
                        },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = isSelected,
                        onClick = null,
                        colors = RadioButtonDefaults.colors(
                            selectedColor = MaterialTheme.colorScheme.primary,
                            unselectedColor = MaterialTheme.colorScheme.onSurfaceVariant
                        ),
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = languageText,
                        style = MaterialTheme.typography.bodyLarge,
                        color = if (isSelected) {
                            MaterialTheme.colorScheme.onPrimaryContainer
                        } else {
                            MaterialTheme.colorScheme.onSurface
                        }
                    )
                }
            }
        }
    }
} 