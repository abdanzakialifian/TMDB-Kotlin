package com.application.tmdb.core.di

import com.application.tmdb.core.domain.interfaces.ITvShowsRepository
import com.application.tmdb.core.repository.TvShowsRepository
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