package com.application.zaki.movies.domain.di

import com.application.zaki.movies.domain.interfaces.ICombineUseCase
import com.application.zaki.movies.domain.usecase.CombineUseCase
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
abstract class CombineUseCaseModule {
    @Binds
    @ViewModelScoped
   abstract fun provideCombineUseCase(combineUseCase: CombineUseCase): ICombineUseCase
}