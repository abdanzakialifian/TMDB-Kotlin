package com.application.tmdb.domain.usecase

import com.application.tmdb.domain.interfaces.IOtherRepository
import io.reactivex.Flowable
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetTMDBTheme @Inject constructor(private val iOtherRepository: IOtherRepository) {
    operator fun invoke(): Flowable<Boolean> = iOtherRepository.getTMDBTheme()
}