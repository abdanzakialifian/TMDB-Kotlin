package com.application.tmdb.domain.usecase

import com.application.tmdb.domain.interfaces.IOtherRepository
import com.application.tmdb.common.model.DetailCastModel
import io.reactivex.Flowable
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetDetailCast @Inject constructor(private val iOtherRepository: IOtherRepository) {
    operator fun invoke(personId: Int): Flowable<com.application.tmdb.common.model.DetailCastModel> =
        iOtherRepository.getDetailCast(personId)
}