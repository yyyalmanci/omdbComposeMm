package com.yyy.domain.usecase

import com.yyy.domain.repository.MoviesRepository
import com.yyy.domain.repository.result.MovieDetailRepositoryResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetMovieDetailUseCase @Inject constructor(
    private val moviesRepository: MoviesRepository
) {
    suspend operator fun invoke(imdbId: String): Flow<MovieDetailRepositoryResult> = flow {
        emit(MovieDetailRepositoryResult.Loading)
        emit(moviesRepository.getMovieDetail(imdbId))
    }
} 