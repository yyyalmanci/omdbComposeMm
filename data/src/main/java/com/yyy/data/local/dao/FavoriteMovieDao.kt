package com.yyy.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.yyy.data.local.entity.FavoriteMovieEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteMovieDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavoriteMovie(movie: FavoriteMovieEntity)

    @Query("DELETE FROM favorite_movies WHERE imdbID = :imdbId")
    suspend fun deleteFavoriteMovie(imdbId: String)

    @Query("SELECT * FROM favorite_movies ORDER BY addedAt DESC")
    fun getAllFavoriteMovies(): Flow<List<FavoriteMovieEntity>>

    @Query("SELECT EXISTS(SELECT 1 FROM favorite_movies WHERE imdbID = :imdbId)")
    suspend fun isMovieFavorite(imdbId: String): Boolean
} 