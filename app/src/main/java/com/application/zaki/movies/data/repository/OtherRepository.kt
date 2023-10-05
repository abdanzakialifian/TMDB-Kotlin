package com.application.zaki.movies.data.repository

import androidx.paging.PagingData
import androidx.paging.map
import com.application.zaki.movies.data.source.remote.RemoteDataSource
import com.application.zaki.movies.domain.interfaces.IOtherRepository
import com.application.zaki.movies.domain.model.DiscoverItem
import com.application.zaki.movies.domain.model.ReviewModel
import com.application.zaki.movies.utils.Category
import com.application.zaki.movies.utils.DataMapper.toResultItemDiscover
import com.application.zaki.movies.utils.DataMapper.toReviewItem
import io.reactivex.Flowable
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class OtherRepository @Inject constructor(private val remoteDataSource: RemoteDataSource) :
    IOtherRepository {
    override fun getReviewsPaging(
        id: String?,
        category: Category?
    ): Flowable<PagingData<ReviewModel>> =
        remoteDataSource.getReviewsPaging(id, category)
            .map { pagingData ->
                pagingData.map { data ->
                    data.toReviewItem()
                }
            }

    override fun getDiscoverPaging(
        genreId: String,
        category: Category
    ): Flowable<PagingData<DiscoverItem>> =
        remoteDataSource.getDiscoverPaging(genreId, category)
            .map { pagingData ->
                pagingData.map { data ->
                    data.toResultItemDiscover()
                }
            }
}