package com.yyy.data.repository

import com.yyy.data.di.IO
import com.yyy.data.local.LocalDataSource
import com.yyy.data.local.entity.FavoriteMovieEntity
import com.yyy.domain.model.MovieSearchResultItem
import com.yyy.domain.repository.FavoritesRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

class FavoritesRepositoryImpl @Inject constructor(
    private val localDataSource: LocalDataSource,
    @IO private val ioDispatcher: CoroutineDispatcher
) : FavoritesRepository {

    override suspend fun addToFavorites(movie: MovieSearchResultItem) = withContext(ioDispatcher) {
        localDataSource.insertFavoriteMovie(movie.toFavoriteMovieEntity())
    }

    override suspend fun removeFromFavorites(movie: MovieSearchResultItem) =
        withContext(ioDispatcher) {
            localDataSource.deleteFavoriteMovie(movie.toFavoriteMovieEntity())
        }

    override fun getAllFavoriteMovies(): Flow<List<MovieSearchResultItem>> =
        localDataSource.getAllFavoriteMovies().map { entities ->
            entities.map { it.toMovieSearchResultItem() }
        }

    private fun MovieSearchResultItem.toFavoriteMovieEntity() = FavoriteMovieEntity(
        imdbID = imdbID,
        title = title,
        year = year,
        type = type,
        poster = poster
    )

    private fun FavoriteMovieEntity.toMovieSearchResultItem() = MovieSearchResultItem(
        imdbID = imdbID,
        title = title,
        year = year,
        type = type,
        poster = poster
    )
} 