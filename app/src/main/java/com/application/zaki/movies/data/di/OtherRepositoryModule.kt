package com.application.zaki.movies.data.di

import com.application.zaki.movies.data.repository.OtherRepository
import com.application.zaki.movies.domain.interfaces.IOtherRepository
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