package com.yyy.data.repository

import com.yyy.data.di.IO
import com.yyy.data.remote.RemoteDataSource
import com.yyy.data.remote.model.MovieSearchResponse
import com.yyy.data.remote.model.toModel
import com.yyy.data.util.NetworkResponse
import com.yyy.data.util.makeCallWithTryCatch
import com.yyy.domain.repository.MoviesRepository
import com.yyy.domain.repository.result.MoviesRepositoryResult
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class MoviesRepositoryImpl @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    @IO private val ioDispatcher: CoroutineDispatcher,
) : MoviesRepository {

    override suspend fun getMovies(query: String, page: Int): MoviesRepositoryResult =
        withContext(ioDispatcher) {
            when (val result = makeCallWithTryCatch { remoteDataSource.getMovies(query, page) }) {
                is NetworkResponse.Success -> {
                    val movies = (result.data as MovieSearchResponse).toModel()
                    MoviesRepositoryResult.Success(movies)
                }

                is NetworkResponse.Error -> {
                    MoviesRepositoryResult.Failed(result.e?.message)
                }
            }
        }
}