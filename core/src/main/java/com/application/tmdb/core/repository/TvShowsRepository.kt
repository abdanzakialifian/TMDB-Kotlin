package com.application.tmdb.core.repository

import androidx.paging.PagingData
import androidx.paging.map
import com.application.tmdb.common.model.DetailModel
import com.application.tmdb.common.utils.DataMapper.toDetailTvShow
import com.application.tmdb.common.utils.DataMapper.toTvShow
import com.application.tmdb.common.utils.Page
import com.application.tmdb.common.utils.TvShow
import com.application.tmdb.core.source.remote.RemoteDataSource
import com.application.tmdb.domain.interfaces.ITvShowsRepository
import io.reactivex.Flowable
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TvShowsRepository @Inject constructor(private val remoteDataSource: RemoteDataSource) :
    ITvShowsRepository {

    override fun getDetailTvShows(tvId: String): Flowable<DetailModel> =
        remoteDataSource.getDetailTvShows(tvId).map { data ->
            data.toDetailTvShow()
        }

    override fun getTvShows(
        tvShow: TvShow?,
        page: Page?,
        query: String?,
        tvId: Int?
    ): Flowable<PagingData<com.application.tmdb.common.model.MovieTvShowModel>> =
        remoteDataSource.getTvShows(tvShow, page, query, tvId).map { pagingData ->
            pagingData.map {
                it.toTvShow()
            }
        }
}