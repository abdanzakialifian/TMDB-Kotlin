package com.application.tmdb.domain.usecase.movie

import com.application.tmdb.domain.usecase.GetListAllMovies
import com.application.tmdb.domain.usecase.GetListMovies
import javax.inject.Inject

data class MovieWrapper @Inject constructor(
    val getListAllMovies: GetListAllMovies,
    val getListMovies: GetListMovies
)
