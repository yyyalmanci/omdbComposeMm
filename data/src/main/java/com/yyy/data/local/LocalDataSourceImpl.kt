package com.yyy.data.local

import com.yyy.data.di.IO
import com.yyy.data.local.dao.FavoriteMovieDao
import com.yyy.data.local.dao.SearchHistoryDao
import com.yyy.data.local.entity.FavoriteMovieEntity
import com.yyy.data.local.entity.SearchHistoryEntity
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class LocalDataSourceImpl @Inject constructor(
    private val searchHistoryDao: SearchHistoryDao,
    private val favoriteMovieDao: FavoriteMovieDao,
    @IO private val ioDispatcher: CoroutineDispatcher
) : LocalDataSource {

    // Search History Operations
    override suspend fun insertSearch(search: SearchHistoryEntity) = withContext(ioDispatcher) {
        searchHistoryDao.insertSearch(search)
    }

    override fun getSearchItems(): Flow<List<SearchHistoryEntity>> =
        searchHistoryDao.getAllSearches()

    // Favorite Movie Operations
    override suspend fun insertFavoriteMovie(movie: FavoriteMovieEntity) =
        withContext(ioDispatcher) {
            favoriteMovieDao.insertFavoriteMovie(movie)
        }

    override suspend fun deleteFavoriteMovie(movie: FavoriteMovieEntity) =
        withContext(ioDispatcher) {
            favoriteMovieDao.deleteFavoriteMovie(movie)
        }

    override fun getAllFavoriteMovies(): Flow<List<FavoriteMovieEntity>> =
        favoriteMovieDao.getAllFavoriteMovies()
} 