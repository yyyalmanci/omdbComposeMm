package com.yyy.data.di

import com.yyy.data.repository.MoviesRepositoryImpl
import com.yyy.domain.repository.MoviesRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
abstract class RepositoryModule {

    @Singleton
    @Binds
    abstract fun bindMoviesRepository(
        impl: MoviesRepositoryImpl
    ): MoviesRepository
} 