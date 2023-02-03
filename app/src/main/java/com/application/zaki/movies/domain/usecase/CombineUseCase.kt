package com.application.zaki.movies.domain.usecase

import androidx.paging.PagingData
import com.application.zaki.movies.domain.interfaces.ICombineRepository
import com.application.zaki.movies.domain.interfaces.ICombineUseCase
import com.application.zaki.movies.domain.model.movies.ResultsItemDiscover
import com.application.zaki.movies.domain.model.movies.ReviewItem
import io.reactivex.Flowable
import javax.inject.Inject

class CombineUseCase @Inject constructor(private val combineRepository: ICombineRepository) :
    ICombineUseCase {
    override fun getReviewsPaging(
        movieId: String,
        totalPage: String,
        type: String
    ): Flowable<PagingData<ReviewItem>> =
        combineRepository.getReviewsPaging(movieId, totalPage, type)

    override fun getDiscoverPaging(
        genreId: String,
        type: String
    ): Flowable<PagingData<ResultsItemDiscover>> =
        combineRepository.getDiscoverPaging(genreId, type)
}