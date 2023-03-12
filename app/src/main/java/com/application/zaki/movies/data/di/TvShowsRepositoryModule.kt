package com.application.zaki.movies.data.di

import com.application.zaki.movies.data.repository.TvShowsRepository
import com.application.zaki.movies.domain.interfaces.ITvShowsRepository
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