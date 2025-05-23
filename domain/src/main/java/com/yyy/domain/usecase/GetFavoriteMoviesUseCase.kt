package com.yyy.domain.usecase

import com.yyy.domain.model.MovieSearchResultItem
import com.yyy.domain.repository.FavoritesRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetFavoriteMoviesUseCase @Inject constructor(
    private val repository: FavoritesRepository
) {
    operator fun invoke(): Flow<List<MovieSearchResultItem>> = repository.getAllFavoriteMovies()
} 