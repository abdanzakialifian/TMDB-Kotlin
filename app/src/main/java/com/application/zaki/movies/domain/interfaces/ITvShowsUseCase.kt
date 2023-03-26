package com.application.zaki.movies.domain.interfaces

import androidx.paging.PagingData
import com.application.zaki.movies.domain.model.tvshows.*
import com.application.zaki.movies.utils.Genre
import com.application.zaki.movies.utils.Page
import com.application.zaki.movies.utils.TvShow
import io.reactivex.Flowable

interface ITvShowsUseCase {
    fun getDetailTvShows(tvId: String): Flowable<DetailTvShows>

    fun getTvShows(tvShow: TvShow, genre: Genre, page: Page): Flowable<PagingData<ListTvShows>>
}