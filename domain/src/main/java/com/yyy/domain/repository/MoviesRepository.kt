package com.yyy.domain.repository

import com.yyy.domain.repository.result.MoviesRepositoryResult


interface MoviesRepository {
    suspend fun getMovies(query: String): MoviesRepositoryResult
} 