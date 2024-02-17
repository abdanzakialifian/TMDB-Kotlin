package com.application.tmdb.domain.interfaces

import androidx.paging.PagingData
import com.application.tmdb.domain.model.DetailModel
import com.application.tmdb.domain.model.MovieTvShowModel
import com.application.tmdb.utils.Page
import com.application.tmdb.utils.TvShow
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