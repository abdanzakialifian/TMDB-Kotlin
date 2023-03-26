package com.application.zaki.movies.domain.interfaces

import androidx.paging.PagingData
import com.application.zaki.movies.domain.model.genre.Genres
import com.application.zaki.movies.domain.model.tvshows.*
import com.application.zaki.movies.utils.Genre
import com.application.zaki.movies.utils.Page
import com.application.zaki.movies.utils.TvShow
import io.reactivex.Flowable

interface ITvShowsRepository {

    fun getDetailTvShows(tvId: String): Flowable<DetailTvShows>

    fun getGenres(genre: Genre): Flowable<Genres>

    fun getTvShows(tvShow: TvShow, page: Page): Flowable<PagingData<ListTvShows>>
}