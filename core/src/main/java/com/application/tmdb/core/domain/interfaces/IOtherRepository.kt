package com.application.tmdb.core.domain.interfaces

import androidx.paging.PagingData
import com.application.tmdb.core.domain.model.DetailCastModel
import com.application.tmdb.core.domain.model.DiscoverItem
import com.application.tmdb.core.domain.model.ReviewModel
import com.application.tmdb.common.Category
import io.reactivex.Flowable

interface IOtherRepository {
    fun getReviewsPaging(
        id: String?,
        category: com.application.tmdb.common.Category?
    ): Flowable<PagingData<ReviewModel>>

    fun getDiscoverPaging(
        genreId: String,
        category: com.application.tmdb.common.Category
    ): Flowable<PagingData<DiscoverItem>>

    fun getDetailCast(
        personId: Int
    ): Flowable<DetailCastModel>
}