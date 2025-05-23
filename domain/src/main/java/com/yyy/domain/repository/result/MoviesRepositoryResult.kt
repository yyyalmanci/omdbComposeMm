package com.yyy.domain.repository.result

import com.yyy.domain.model.MovieListItem


sealed class MoviesRepositoryResult {
    data object Loading : MoviesRepositoryResult()
    data class Failed(val message: String?) : MoviesRepositoryResult()
    data class Success(val movie: MovieListItem) : MoviesRepositoryResult()
}