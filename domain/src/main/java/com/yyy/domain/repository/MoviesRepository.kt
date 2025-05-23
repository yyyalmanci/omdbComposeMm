package com.yyy.domain.repository

import com.yyy.domain.repository.result.MoviesRepositoryResult
import kotlinx.coroutines.flow.Flow

interface MoviesRepository {
    suspend fun getMovies(query: String, page: Int): MoviesRepositoryResult

    fun getSearchHistory(): Flow<List<String>>
} 