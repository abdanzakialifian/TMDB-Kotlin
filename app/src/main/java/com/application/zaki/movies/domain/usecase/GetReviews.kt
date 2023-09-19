package com.application.zaki.movies.domain.usecase

import androidx.paging.PagingData
import com.application.zaki.movies.domain.interfaces.IOtherRepository
import com.application.zaki.movies.domain.model.other.ReviewItem
import com.application.zaki.movies.utils.Category
import com.application.zaki.movies.utils.Page
import io.reactivex.Flowable
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetReviews @Inject constructor(private val iOtherRepository: IOtherRepository) {
    operator fun invoke(
        id: String,
        page: Page,
        category: Category
    ): Flowable<PagingData<ReviewItem>> =
        iOtherRepository.getReviewsPaging(id, page, category)
}