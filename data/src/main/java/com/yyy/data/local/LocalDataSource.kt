package com.yyy.data.local

import com.yyy.data.local.entity.SearchHistoryEntity
import kotlinx.coroutines.flow.Flow

interface LocalDataSource {
    suspend fun insertSearch(search: SearchHistoryEntity)

    fun getSearchItems(): Flow<List<SearchHistoryEntity>>
} 