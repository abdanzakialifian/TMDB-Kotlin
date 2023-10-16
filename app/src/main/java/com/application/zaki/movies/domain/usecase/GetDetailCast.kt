package com.application.zaki.movies.domain.usecase

import com.application.zaki.movies.domain.interfaces.IOtherRepository
import com.application.zaki.movies.domain.model.DetailCastModel
import io.reactivex.Flowable
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetDetailCast @Inject constructor(private val iOtherRepository: IOtherRepository) {
    operator fun invoke(personId: Int): Flowable<DetailCastModel> =
        iOtherRepository.getDetailCast(personId)
}