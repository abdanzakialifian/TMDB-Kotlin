package com.application.tmdb.core.di

import com.application.tmdb.core.repository.TvShowsRepository
import com.application.tmdb.domain.interfaces.ITvShowsRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class TvShowsRepositoryModule {
    @Binds
    @Singleton
    abstract fun provideTvShowsRepository(tvShowsRepository: TvShowsRepository): ITvShowsRepository
}