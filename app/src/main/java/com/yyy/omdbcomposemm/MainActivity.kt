package com.yyy.omdbcomposemm

import NavGraph
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.yyy.theme.LangOption
import com.yyy.theme.OmdbComposeMmTheme
import com.yyy.ui.language.LanguageManager
import com.yyy.ui.route.RouteClass
import com.yyy.ui.viewmodel.MovieSearchViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var languageManager: LanguageManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val movieSearchViewModel: MovieSearchViewModel = hiltViewModel()
            val currentTheme by movieSearchViewModel.themeState.collectAsState()
            val currentLanguage by movieSearchViewModel.langState.collectAsState()
            val navController = rememberNavController()
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            var selectedTabIndex by rememberSaveable { mutableIntStateOf(0) }
            val tabs = listOf("Films", "Favorites", "Settings")

            LaunchedEffect(Unit) {
                if (currentLanguage.isEmpty()) {
                    languageManager.applyLanguage(LangOption.TR.code)
                }
            }

            OmdbComposeMmTheme(currentTheme) {
                Column {
                    if (navBackStackEntry?.arguments?.getString("name") == RouteClass.MovieSearch().name || navBackStackEntry?.arguments?.getString(
                            "name"
                        ) == RouteClass.FavoritesList().name || navBackStackEntry?.arguments?.getString(
                            "name"
                        ) == RouteClass.Settings().name
                    ) {
                        TabRow(selectedTabIndex = selectedTabIndex) {
                            tabs.forEachIndexed { index, title ->
                                Tab(
                                    selected = selectedTabIndex == index,
                                    onClick = { selectedTabIndex = index },
                                    text = {
                                        Text(
                                            style = MaterialTheme.typography.titleMedium,
                                            text = title
                                        )
                                    }
                                )
                            }
                        }
                    }

                    NavGraph(
                        navController = navController,
                        movieSearchViewModel = movieSearchViewModel,
                        themeOption = currentTheme
                    )

                    if (navBackStackEntry?.arguments?.getString("name") == RouteClass.MovieSearch().name || navBackStackEntry?.arguments?.getString(
                            "name"
                        ) == RouteClass.FavoritesList().name || navBackStackEntry?.arguments?.getString(
                            "name"
                        ) == RouteClass.Settings().name
                    ) {
                        when (selectedTabIndex) {
                            0 -> {
                                if (navBackStackEntry?.arguments?.getString("name") != RouteClass.MovieSearch().name) {
                                    navController.navigate(RouteClass.MovieSearch())
                                }
                            }

                            1 -> {
                                if (navBackStackEntry?.arguments?.getString("name") != RouteClass.FavoritesList().name) {
                                    navController.navigate(RouteClass.FavoritesList())
                                }
                            }

                            2 -> {
                                if (navBackStackEntry?.arguments?.getString("name") != RouteClass.Settings().name) {
                                    navController.navigate(RouteClass.Settings())
                                }
                            }
                        }
                    }
                }

            }
        }
    }
}