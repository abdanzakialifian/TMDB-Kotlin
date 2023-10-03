package com.application.zaki.movies.domain.interfaces

import androidx.paging.PagingData
import com.application.zaki.movies.domain.model.DiscoverItem
import com.application.zaki.movies.domain.model.ReviewItem
import com.application.zaki.movies.utils.Category
import com.application.zaki.movies.utils.Page
import io.reactivex.Flowable

interface IOtherRepository {
    fun getReviewsPaging(
        id: String?,
        category: Category?
    ): Flowable<PagingData<ReviewItem>>

    fun getDiscoverPaging(
        genreId: String,
        category: Category
    ): Flowable<PagingData<DiscoverItem>>
}