package com.yyy.data.di

import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.Dispatchers
import javax.inject.Qualifier
import dagger.Module

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class IO

@InstallIn(SingletonComponent::class)
@Module
class DispatchersModule {
    @IO
    @Provides
    fun provideIODispatcher() = Dispatchers.IO
}