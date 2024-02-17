package com.application.tmdb.core.domain.usecase.movietvshow

import com.application.tmdb.core.domain.usecase.GetListMovies
import com.application.tmdb.core.domain.usecase.GetListTvShows
import javax.inject.Inject

data class MovieTvShowWrapper @Inject constructor(
    val getListMovies: GetListMovies,
    val getListTvShows: GetListTvShows
)
