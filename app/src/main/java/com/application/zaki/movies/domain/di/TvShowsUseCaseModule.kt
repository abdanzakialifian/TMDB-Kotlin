package com.application.zaki.movies.domain.di

import com.application.zaki.movies.domain.interfaces.ITvShowsUseCase
import com.application.zaki.movies.domain.usecase.TvShowsUseCase
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
abstract class TvShowsUseCaseModule {
    @Binds
    @ViewModelScoped
    abstract fun provideUseCaseTvShows(tvShowsUseCase: TvShowsUseCase): ITvShowsUseCase
}