package com.application.tmdb.core.di

import com.application.tmdb.core.domain.interfaces.IOtherRepository
import com.application.tmdb.core.repository.OtherRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class OtherRepositoryModule {
    @Binds
    @Singleton
    abstract fun provideOtherRepository(otherRepository: OtherRepository): IOtherRepository
}