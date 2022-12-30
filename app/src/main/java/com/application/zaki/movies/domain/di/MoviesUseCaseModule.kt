package com.application.zaki.movies.domain.di

import com.application.zaki.movies.domain.interfaces.IMoviesUseCase
import com.application.zaki.movies.domain.usecase.MoviesUseCase
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
abstract class MoviesUseCaseModule {
    @Binds
    @ViewModelScoped
    abstract fun provideUseCaseTvShows(moviesUseCase: MoviesUseCase): IMoviesUseCase
}