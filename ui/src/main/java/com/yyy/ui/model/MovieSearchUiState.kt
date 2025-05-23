package com.yyy.ui.model

import com.yyy.domain.model.MovieListItem

data class MovieSearchUiState(
    val movies: MovieListItem = MovieListItem(
        search = emptyList(),
        totalResults = "",
        response = ""
    ),
    val isLoading: Boolean = false,
    val showFilmNotFound: Boolean = false,
    val searchHistory: List<String> = emptyList(),
    val favoriteMovieIds: Set<String> = emptySet()
)