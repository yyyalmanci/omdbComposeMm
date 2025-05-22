package com.yyy.data.remote

import com.yyy.data.remote.model.MovieDetailResponse
import retrofit2.Response

interface RemoteDataSource {
    suspend fun getMovies(query: String): Response<MovieDetailResponse>
}

