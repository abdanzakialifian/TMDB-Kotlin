package com.application.zaki.movies.domain.usecase.detail

import com.application.zaki.movies.domain.usecase.GetDetailMovie
import com.application.zaki.movies.domain.usecase.GetDetailTvShow
import com.application.zaki.movies.domain.usecase.GetListMovies
import com.application.zaki.movies.domain.usecase.GetListTvShows
import com.application.zaki.movies.domain.usecase.GetReviews
import javax.inject.Inject

data class DetailWrapper @Inject constructor(
    val getDetailMovie: GetDetailMovie,
    val getDetailTvShow: GetDetailTvShow,
    val getReviews: GetReviews,
    val getListMovies: GetListMovies,
    val getListTvShows: GetListTvShows
)
