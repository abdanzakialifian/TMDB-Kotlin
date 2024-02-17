package com.application.tmdb.core.domain.usecase

import androidx.paging.PagingData
import com.application.tmdb.core.domain.interfaces.IOtherRepository
import com.application.tmdb.core.domain.model.ReviewModel
import com.application.tmdb.core.utils.Category
import io.reactivex.Flowable
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetReviews @Inject constructor(private val iOtherRepository: IOtherRepository) {
    operator fun invoke(
        id: String?,
        category: Category?
    ): Flowable<PagingData<ReviewModel>> =
        iOtherRepository.getReviewsPaging(id, category)
}