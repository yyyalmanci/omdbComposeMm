package com.yyy.data.repository

import com.yyy.data.di.IO
import com.yyy.data.local.LocalDataSource
import com.yyy.data.local.entity.SearchHistoryEntity
import com.yyy.data.remote.RemoteDataSource
import com.yyy.data.remote.model.MovieSearchResponse
import com.yyy.data.remote.model.toModel
import com.yyy.data.util.NetworkResponse
import com.yyy.data.util.makeCallWithTryCatch
import com.yyy.domain.repository.MoviesRepository
import com.yyy.domain.repository.result.MoviesRepositoryResult
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

class MoviesRepositoryImpl @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource,
    @IO private val ioDispatcher: CoroutineDispatcher,
) : MoviesRepository {

    override suspend fun getMovies(query: String, page: Int): MoviesRepositoryResult =
        withContext(ioDispatcher) {
            when (val result = makeCallWithTryCatch { remoteDataSource.getMovies(query, page) }) {
                is NetworkResponse.Success -> {
                    val movies = (result.data as MovieSearchResponse).toModel()

                    localDataSource.insertSearch(
                        SearchHistoryEntity(
                            query = query
                        )
                    )
                    MoviesRepositoryResult.Success(movies)
                }

                is NetworkResponse.Error -> {
                    MoviesRepositoryResult.Failed(result.e?.message)
                }
            }
        }

    override fun getSearchHistory(): Flow<List<String>> =
        localDataSource.getSearchItems().map { entities ->
            entities
                .sortedByDescending { it.timestamp }
                .take(10)
                .map { it.query }
        }
}