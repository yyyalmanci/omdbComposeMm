package com.yyy.domain.usecase

import com.yyy.domain.repository.MoviesRepository
import com.yyy.domain.repository.result.MoviesRepositoryResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class SearchMoviesUseCase @Inject constructor(
    private val repository: MoviesRepository
) {
    operator fun invoke(query: String, page: Int): Flow<MoviesRepositoryResult> = flow {
        emit(MoviesRepositoryResult.Loading)
        emit(repository.getMovies(query, page))
    }
}