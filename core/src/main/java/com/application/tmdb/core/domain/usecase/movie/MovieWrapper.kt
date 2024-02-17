package com.application.tmdb.core.domain.usecase.movie

import com.application.tmdb.core.domain.usecase.GetListAllMovies
import com.application.tmdb.core.domain.usecase.GetListMovies
import javax.inject.Inject

data class MovieWrapper @Inject constructor(
    val getListAllMovies: GetListAllMovies,
    val getListMovies: GetListMovies
)
