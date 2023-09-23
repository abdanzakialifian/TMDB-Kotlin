package com.application.zaki.movies.domain.interfaces

import androidx.paging.PagingData
import com.application.zaki.movies.domain.model.Detail
import com.application.zaki.movies.domain.model.Genres
import com.application.zaki.movies.domain.model.MovieTvShow
import com.application.zaki.movies.utils.Category
import com.application.zaki.movies.utils.Page
import com.application.zaki.movies.utils.TvShow
import io.reactivex.Flowable

interface ITvShowsRepository {

    fun getDetailTvShows(tvId: String): Flowable<Detail>

    fun getGenres(category: Category): Flowable<Genres>

    fun getTvShows(tvShow: TvShow, page: Page): Flowable<PagingData<MovieTvShow>>
}