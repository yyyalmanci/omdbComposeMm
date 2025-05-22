package com.yyy.domain.repository.result

sealed class MoviesRepositoryResult {
    data object Loading : MoviesRepositoryResult()
    data class Failed(val message: String?) : MoviesRepositoryResult()
    data object Success : MoviesRepositoryResult()
}