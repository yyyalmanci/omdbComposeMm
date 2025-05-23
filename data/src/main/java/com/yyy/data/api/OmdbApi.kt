package com.yyy.data.api

import com.yyy.data.remote.model.MovieDetailResponse
import com.yyy.data.remote.model.MovieSearchResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface OmdbApi {
    @GET("/")
    suspend fun getMovies(
        @Query("s") query: String,
        @Query("page") page: Int,
    ): Response<MovieSearchResponse>

    @GET("/")
    suspend fun getMovie(
        @Query("i") id: String,
    ): Response<MovieDetailResponse>
} 