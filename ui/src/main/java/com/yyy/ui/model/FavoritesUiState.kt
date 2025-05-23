package com.yyy.ui.model

import com.yyy.domain.model.MovieSearchResultItem

data class FavoritesUiState(
    val movies: List<MovieSearchResultItem> = emptyList(),
    val isLoading: Boolean = false
)