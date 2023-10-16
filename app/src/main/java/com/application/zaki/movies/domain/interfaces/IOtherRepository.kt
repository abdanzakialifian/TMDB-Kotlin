package com.application.zaki.movies.domain.interfaces

import androidx.paging.PagingData
import com.application.zaki.movies.domain.model.DetailCastModel
import com.application.zaki.movies.domain.model.DiscoverItem
import com.application.zaki.movies.domain.model.ReviewModel
import com.application.zaki.movies.utils.Category
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