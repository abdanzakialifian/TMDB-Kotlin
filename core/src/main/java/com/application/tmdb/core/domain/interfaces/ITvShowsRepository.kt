package com.application.tmdb.core.domain.interfaces

import androidx.paging.PagingData
import com.application.tmdb.core.domain.model.DetailModel
import com.application.tmdb.core.domain.model.MovieTvShowModel
import com.application.tmdb.core.utils.Page
import com.application.tmdb.core.utils.TvShow
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