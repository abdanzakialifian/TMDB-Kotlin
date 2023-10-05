package com.application.zaki.movies.domain.interfaces

import androidx.paging.PagingData
import com.application.zaki.movies.domain.model.DetailModel
import com.application.zaki.movies.domain.model.MovieTvShowModel
import com.application.zaki.movies.utils.Page
import com.application.zaki.movies.utils.TvShow
import io.reactivex.Flowable

interface ITvShowsRepository {

    fun getDetailTvShows(tvId: String): Flowable<DetailModel>

    fun getTvShows(
        tvShow: TvShow?,
        page: Page?,
        query: String?,
        tvId: Int?
    ): Flowable<PagingData<MovieTvShowModel>>
}