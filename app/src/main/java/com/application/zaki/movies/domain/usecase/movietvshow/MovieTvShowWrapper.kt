package com.application.zaki.movies.domain.usecase.movietvshow

import com.application.zaki.movies.domain.usecase.GetListMovies
import com.application.zaki.movies.domain.usecase.GetListTvShows
import javax.inject.Inject

data class MovieTvShowWrapper @Inject constructor(
    val getListMovies: GetListMovies,
    val getListTvShows: GetListTvShows
)
