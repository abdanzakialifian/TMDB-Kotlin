package com.application.tmdb.core.domain.interfaces

import androidx.paging.PagingData
import com.application.tmdb.core.domain.model.DetailCastModel
import com.application.tmdb.core.domain.model.DiscoverItem
import com.application.tmdb.core.domain.model.ReviewModel
import com.application.tmdb.core.utils.Category
import io.reactivex.Flowable

interface IOtherRepository {
    fun getReviewsPaging(
        id: String?,
        category: Category?
    ): Flowable<PagingData<ReviewModel>>

    fun getDiscoverPaging(
        genreId: String,
        category: Category
    ): Flowable<PagingData<DiscoverItem>>

    fun getDetailCast(
        personId: Int
    ): Flowable<DetailCastModel>
}