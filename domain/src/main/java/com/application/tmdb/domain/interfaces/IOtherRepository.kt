package com.application.tmdb.domain.interfaces

import androidx.paging.PagingData
import com.application.tmdb.common.model.DetailCastModel
import com.application.tmdb.common.model.DiscoverItem
import com.application.tmdb.common.model.ReviewModel
import com.application.tmdb.common.utils.Category
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

    fun saveTMDBTheme(isDarkMode: Boolean)

    fun getTMDBTheme(): Flowable<Boolean>
}