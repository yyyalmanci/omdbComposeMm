package com.yyy.data.di

import com.yyy.data.repository.MoviesRepository
import com.yyy.data.repository.MoviesRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract fun bindMoviesRepository(
        impl: MoviesRepositoryImpl
    ): MoviesRepository
} 