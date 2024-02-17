package com.application.tmdb.domain.usecase.tvshow

import com.application.tmdb.domain.usecase.GetListAllTvShows
import com.application.tmdb.domain.usecase.GetListTvShows
import javax.inject.Inject

data class TvShowWrapper @Inject constructor(
    val getListAllTvShows: GetListAllTvShows,
    val getListTvShows: GetListTvShows
)