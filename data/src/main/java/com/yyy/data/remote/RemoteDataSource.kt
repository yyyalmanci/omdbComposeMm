package com.yyy.data.remote

import com.yyy.data.remote.model.MovieDetailResponse
import com.yyy.data.remote.model.MovieSearchResponse
import retrofit2.Response

interface RemoteDataSource {
    suspend fun getMovies(query: String, page: Int): Response<MovieSearchResponse>

    suspend fun getMovie(imdbId: String): Response<MovieDetailResponse>
}

