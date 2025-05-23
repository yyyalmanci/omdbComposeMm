package com.yyy.ui.model

import com.yyy.domain.model.MovieDetail

data class MovieDetailUiState(
    val isLoading: Boolean = false,
    val movie: MovieDetail? = null,
    val error: String? = null
)