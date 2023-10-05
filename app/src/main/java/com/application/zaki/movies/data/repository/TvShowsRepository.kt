package com.application.zaki.movies.data.repository

import androidx.paging.PagingData
import androidx.paging.map
import com.application.zaki.movies.data.source.remote.RemoteDataSource
import com.application.zaki.movies.domain.interfaces.ITvShowsRepository
import com.application.zaki.movies.domain.model.DetailModel
import com.application.zaki.movies.domain.model.MovieTvShowModel
import com.application.zaki.movies.utils.DataMapper.toDetailTvShow
import com.application.zaki.movies.utils.DataMapper.toTvShow
import com.application.zaki.movies.utils.Page
import com.application.zaki.movies.utils.TvShow
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