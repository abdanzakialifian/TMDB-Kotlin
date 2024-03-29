package com.application.tmdb.core.repository

import androidx.paging.PagingData
import androidx.paging.map
import com.application.tmdb.common.model.DetailCastModel
import com.application.tmdb.common.model.DiscoverItem
import com.application.tmdb.common.model.ReviewModel
import com.application.tmdb.common.utils.Category
import com.application.tmdb.common.utils.DataMapper.toDetailCastModel
import com.application.tmdb.common.utils.DataMapper.toResultItemDiscover
import com.application.tmdb.common.utils.DataMapper.toReviewItem
import com.application.tmdb.core.source.remote.RemoteDataSource
import com.application.tmdb.domain.interfaces.IOtherRepository
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

    override fun getDetailCast(personId: Int): Flowable<DetailCastModel> =
        remoteDataSource.getDetailCast(personId).map { data ->
            data.toDetailCastModel()
        }
}