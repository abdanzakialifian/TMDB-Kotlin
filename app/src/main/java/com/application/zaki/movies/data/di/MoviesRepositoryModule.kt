package com.application.zaki.movies.data.di

import com.application.zaki.movies.domain.interfaces.IMoviesRepository
import com.application.zaki.movies.data.repository.MoviesRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class MoviesRepositoryModule {
    @Binds
    @Singleton
    abstract fun provideMoviesRepository(moviesRepository: MoviesRepository): IMoviesRepository
}