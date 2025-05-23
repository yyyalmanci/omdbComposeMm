package com.yyy.data.local

import com.yyy.data.local.entity.FavoriteMovieEntity
import com.yyy.data.local.entity.SearchHistoryEntity
import kotlinx.coroutines.flow.Flow

interface LocalDataSource {
    // Search History Operations
    suspend fun insertSearch(search: SearchHistoryEntity)
    fun getSearchItems(): Flow<List<SearchHistoryEntity>>

    // Favorite Movie Operations
    suspend fun insertFavoriteMovie(movie: FavoriteMovieEntity)
    suspend fun deleteFavoriteMovie(imdbId: String)
    fun getAllFavoriteMovies(): Flow<List<FavoriteMovieEntity>>
}