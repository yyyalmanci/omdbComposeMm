package com.yyy.ui.model

import com.yyy.domain.model.MovieListItem

data class MovieSearchUiState(
    val isLoading: Boolean = false,
    val showFilmNotFound: Boolean = false,
    val movies: MovieListItem = MovieListItem(
        search = emptyList(),
        totalResults = "",
        response = ""
    ),
    val searchHistory: List<String> = emptyList()
)