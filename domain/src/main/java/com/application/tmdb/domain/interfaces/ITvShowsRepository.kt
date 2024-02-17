package com.application.tmdb.domain.interfaces

import androidx.paging.PagingData
import com.application.tmdb.common.model.DetailModel
import com.application.tmdb.common.model.MovieTvShowModel
import com.application.tmdb.common.utils.Page
import com.application.tmdb.common.utils.TvShow
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