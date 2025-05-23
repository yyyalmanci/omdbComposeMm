package com.yyy.domain.repository

import com.yyy.domain.model.MovieSearchResultItem
import kotlinx.coroutines.flow.Flow

interface FavoritesRepository {
    suspend fun addToFavorites(movie: MovieSearchResultItem)
    suspend fun removeFromFavorites(movie: MovieSearchResultItem)
    fun getAllFavoriteMovies(): Flow<List<MovieSearchResultItem>>
} 