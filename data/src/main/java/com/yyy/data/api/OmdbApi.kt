package com.yyy.data.api

import com.yyy.data.remote.model.MovieDetailResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface OmdbApi {
    @GET()
    suspend fun getMovies(
        @Query("s") query: String
    ): Response<MovieDetailResponse>
} 