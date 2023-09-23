package com.application.zaki.movies.data.repository

import androidx.paging.PagingData
import androidx.paging.map
import com.application.zaki.movies.data.source.remote.RemoteDataSource
import com.application.zaki.movies.domain.interfaces.ITvShowsRepository
import com.application.zaki.movies.domain.model.Detail
import com.application.zaki.movies.domain.model.Genres
import com.application.zaki.movies.domain.model.MovieTvShow
import com.application.zaki.movies.utils.Category
import com.application.zaki.movies.utils.DataMapper.toDetailTvShow
import com.application.zaki.movies.utils.DataMapper.toGenres
import com.application.zaki.movies.utils.DataMapper.toTvShow
import com.application.zaki.movies.utils.Page
import com.application.zaki.movies.utils.TvShow
import io.reactivex.Flowable
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TvShowsRepository @Inject constructor(private val remoteDataSource: RemoteDataSource) :
    ITvShowsRepository {

    override fun getDetailTvShows(tvId: String): Flowable<Detail> =
        remoteDataSource.getDetailTvShows(tvId).map { data ->
            data.toDetailTvShow()
        }

    override fun getGenres(category: Category): Flowable<Genres> =
        remoteDataSource.getGenre(category)
            .map {
                it.toGenres()
            }

    override fun getTvShows(tvShow: TvShow, page: Page): Flowable<PagingData<MovieTvShow>> =
        remoteDataSource.getTvShows(tvShow, page).map { pagingData ->
            pagingData.map {
                it.toTvShow()
            }
        }
}