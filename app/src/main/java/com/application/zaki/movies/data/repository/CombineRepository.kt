package com.application.zaki.movies.data.repository

import androidx.paging.PagingData
import androidx.paging.map
import com.application.zaki.movies.data.source.remote.RemoteDataSource
import com.application.zaki.movies.domain.interfaces.ICombineRepository
import com.application.zaki.movies.domain.model.movies.ResultsItemDiscover
import com.application.zaki.movies.domain.model.movies.ReviewItem
import com.application.zaki.movies.utils.DataMapperMovies
import io.reactivex.Flowable
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CombineRepository @Inject constructor(private val remoteDataSource: RemoteDataSource) :
    ICombineRepository {
    override fun getReviewsPaging(
        movieId: String,
        totalPage: String,
        type: String
    ): Flowable<PagingData<ReviewItem>> =
        remoteDataSource.getReviewsPaging(movieId, totalPage, type)
            .map { pagingData ->
                pagingData.map {
                    DataMapperMovies.mapReviewMovieResponseToReviewMovie(it)
                }
            }

    override fun getDiscoverPaging(
        genreId: String,
        type: String
    ): Flowable<PagingData<ResultsItemDiscover>> =
        remoteDataSource.getDiscoverPaging(genreId, type)
            .map { pagingData ->
                pagingData.map { map ->
                    DataMapperMovies.mapResultItemDiscoverResponseToResultItemDiscover(map)
                }
            }
}