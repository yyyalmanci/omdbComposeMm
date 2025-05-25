package com.yyy.ui.model

import com.yyy.domain.model.MovieListItem

data class MovieSearchUiState(
    val movies: MovieListItem = MovieListItem(
        search = emptyList()
    ),
    val isLoading: Boolean = false,
    val showFilmNotFound: Boolean = false,
    val searchHistory: List<String> = emptyList(),
    val favoriteMovieIds: Set<FavoriteMovieId> = emptySet(),
    val typeFilter: String? = null,
    val yearFilter: String? = null,
    val sortOption: SortOption = SortOption.NONE
)

data class FavoriteMovieId(
    val imdbId: String,
    val listTitle: String
)

enum class MovieType(val type: String) {
    MOVIE("movie"),
    SERIES("series"),
    EPISODE("episode")
}

enum class SortOption {
    NONE,
    YEAR,
    A_TO_Z,
    Z_TO_A
}