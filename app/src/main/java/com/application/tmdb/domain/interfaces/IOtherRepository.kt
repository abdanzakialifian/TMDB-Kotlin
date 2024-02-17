package com.application.tmdb.domain.interfaces

import androidx.paging.PagingData
import com.application.tmdb.domain.model.DetailCastModel
import com.application.tmdb.domain.model.DiscoverItem
import com.application.tmdb.domain.model.ReviewModel
import com.application.tmdb.utils.Category
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