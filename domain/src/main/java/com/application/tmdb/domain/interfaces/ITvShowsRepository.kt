package com.application.tmdb.domain.interfaces

import androidx.paging.PagingData
import com.application.tmdb.core.utils.Page
import com.application.tmdb.core.utils.TvShow
import com.application.tmdb.domain.model.DetailModel
import com.application.tmdb.domain.model.MovieTvShowModel
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