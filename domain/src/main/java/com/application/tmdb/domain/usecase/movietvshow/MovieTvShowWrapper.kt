package com.application.tmdb.domain.usecase.movietvshow

import com.application.tmdb.domain.usecase.GetListMovies
import com.application.tmdb.domain.usecase.GetListTvShows
import javax.inject.Inject

data class MovieTvShowWrapper @Inject constructor(
    val getListMovies: GetListMovies,
    val getListTvShows: GetListTvShows
)
