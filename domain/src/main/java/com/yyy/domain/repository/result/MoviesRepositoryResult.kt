package com.yyy.domain.repository.result

import com.yyy.domain.model.MovieDetail
import com.yyy.domain.model.MovieListItem


sealed class MoviesRepositoryResult {
    data object Loading : MoviesRepositoryResult()
    data class Failed(val message: String?) : MoviesRepositoryResult()
    data class Success(val movie: MovieListItem) : MoviesRepositoryResult()
}

sealed class MovieDetailRepositoryResult {
    data object Loading : MovieDetailRepositoryResult()
    data class Failed(val message: String?) : MovieDetailRepositoryResult()
    data class Success(val movie: MovieDetail) : MovieDetailRepositoryResult()
}