package com.application.tmdb.core.di

import com.application.tmdb.core.repository.MoviesRepository
import com.application.tmdb.domain.interfaces.IMoviesRepository
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