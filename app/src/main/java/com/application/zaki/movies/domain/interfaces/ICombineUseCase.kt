package com.application.zaki.movies.domain.interfaces

import androidx.paging.PagingData
import com.application.zaki.movies.domain.model.movies.ResultsItemDiscover
import com.application.zaki.movies.domain.model.movies.ReviewItem
import io.reactivex.Flowable

interface ICombineUseCase {
    fun getReviewsPaging(
        movieId: String,
        totalPage: String,
        type: String
    ): Flowable<PagingData<ReviewItem>>

    fun getDiscoverPaging(genreId: String, type: String): Flowable<PagingData<ResultsItemDiscover>>
}