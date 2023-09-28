package com.application.zaki.movies.domain.usecase.tvshow

import com.application.zaki.movies.domain.usecase.GetListAllTvShows
import com.application.zaki.movies.domain.usecase.GetListTvShows
import javax.inject.Inject

data class TvShowWrapper @Inject constructor(
    val getListAllTvShows: GetListAllTvShows,
    val getListTvShows: GetListTvShows
)