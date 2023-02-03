package com.application.zaki.movies.data.di

import com.application.zaki.movies.data.repository.CombineRepository
import com.application.zaki.movies.domain.interfaces.ICombineRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class CombineRepositoryModule {
    @Binds
    @Singleton
    abstract fun provideCombineRepository(combineRepository: CombineRepository): ICombineRepository
}