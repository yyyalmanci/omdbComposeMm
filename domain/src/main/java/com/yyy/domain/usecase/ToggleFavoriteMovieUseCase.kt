package com.yyy.domain.usecase

import com.yyy.domain.model.MovieSearchResultItem
import com.yyy.domain.repository.FavoritesRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class ToggleFavoriteMovieUseCase @Inject constructor(
    private val repository: FavoritesRepository
) {
    suspend operator fun invoke(movie: MovieSearchResultItem) {
        val favoriteMovies = repository.getAllFavoriteMovies().first()
        if (favoriteMovies.any { it.imdbID == movie.imdbID }) {
            repository.removeFromFavorites(movie)
        } else {
            repository.addToFavorites(movie)
        }
    }
} 