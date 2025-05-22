package com.yyy.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import com.yyy.ui.viewmodel.MovieSearchViewModel


@Composable
fun MovieSearchScreen(viewModel: MovieSearchViewModel = hiltViewModel()) {

    Column {
        Text(text = "MovieSearchScreen")
    }
}