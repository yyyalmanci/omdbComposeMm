package com.yyy.data.local

import com.yyy.data.di.IO
import com.yyy.data.local.dao.SearchHistoryDao
import com.yyy.data.local.entity.SearchHistoryEntity
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class LocalDataSourceImpl @Inject constructor(
    private val searchHistoryDao: SearchHistoryDao,
    @IO private val ioDispatcher: CoroutineDispatcher
) : LocalDataSource {

    override suspend fun insertSearch(search: SearchHistoryEntity) = withContext(ioDispatcher) {
        searchHistoryDao.insertSearch(search)
    }

    override fun getSearchItems(): Flow<List<SearchHistoryEntity>> =
        searchHistoryDao.getAllSearches()
} 