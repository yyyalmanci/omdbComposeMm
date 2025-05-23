package com.yyy.domain.repository

import com.yyy.domain.model.MovieSearchResultItem
import kotlinx.coroutines.flow.Flow

interface FavoritesRepository {
    suspend fun addToFavorites(movie: MovieSearchResultItem, listTitle: String)
    suspend fun removeFromFavorites(imdbId: String)
    fun getAllFavoriteMovies(): Flow<List<MovieSearchResultItem>>
} 