package com.application.tmdb.core.domain.usecase.detail

import com.application.tmdb.core.domain.usecase.GetDetailMovie
import com.application.tmdb.core.domain.usecase.GetDetailTvShow
import com.application.tmdb.core.domain.usecase.GetListMovies
import com.application.tmdb.core.domain.usecase.GetListTvShows
import com.application.tmdb.core.domain.usecase.GetReviews
import javax.inject.Inject

data class DetailWrapper @Inject constructor(
    val getDetailMovie: GetDetailMovie,
    val getDetailTvShow: GetDetailTvShow,
    val getReviews: GetReviews,
    val getListMovies: GetListMovies,
    val getListTvShows: GetListTvShows
)
