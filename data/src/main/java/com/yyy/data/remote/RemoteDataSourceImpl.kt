package com.yyy.data.remote

import com.yyy.data.api.OmdbApi
import javax.inject.Inject

class RemoteDataSourceImpl @Inject constructor(
    private val omdbApi: OmdbApi
) : RemoteDataSource {
    override suspend fun getMovies(query: String, page: Int) =
        omdbApi.getMovies(query, page)

    override suspend fun getMovie(imdbId: String) =
        omdbApi.getMovie(imdbId)
}