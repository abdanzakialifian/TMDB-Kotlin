package com.application.tmdb.core.repository

import androidx.paging.PagingData
import androidx.paging.map
import com.application.tmdb.core.domain.interfaces.ITvShowsRepository
import com.application.tmdb.core.domain.model.DetailModel
import com.application.tmdb.core.domain.model.MovieTvShowModel
import com.application.tmdb.core.utils.DataMapper.toDetailTvShow
import com.application.tmdb.core.utils.DataMapper.toTvShow
import com.application.tmdb.core.utils.Page
import com.application.tmdb.core.utils.TvShow
import com.application.tmdb.core.source.remote.RemoteDataSource
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

    override fun getTvShows(tvShow: TvShow?, page: Page?, query: String?, tvId: Int?): Flowable<PagingData<MovieTvShowModel>> =
        remoteDataSource.getTvShows(tvShow, page, query, tvId).map { pagingData ->
            pagingData.map {
                it.toTvShow()
            }
        }
}