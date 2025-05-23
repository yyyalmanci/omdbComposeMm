package com.yyy.data.di

import android.content.Context
import androidx.room.Room
import com.yyy.data.local.AppDatabase
import com.yyy.data.local.LocalDataSource
import com.yyy.data.local.LocalDataSourceImpl
import com.yyy.data.local.dao.SearchHistoryDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(
        @ApplicationContext context: Context
    ): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "omdb_database"
        ).build()
    }

    @Provides
    @Singleton
    fun provideSearchHistoryDao(database: AppDatabase) = database.searchHistoryDao()

    @Provides
    @Singleton
    fun provideLocalDataSource(
        searchHistoryDao: SearchHistoryDao,
        @IO ioDispatcher: CoroutineDispatcher
    ): LocalDataSource {
        return LocalDataSourceImpl(searchHistoryDao, ioDispatcher)
    }
} 