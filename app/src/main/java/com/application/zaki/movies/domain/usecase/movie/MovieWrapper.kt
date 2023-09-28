package com.application.zaki.movies.domain.usecase.movie

import com.application.zaki.movies.domain.usecase.GetListAllMovies
import com.application.zaki.movies.domain.usecase.GetListMovies
import javax.inject.Inject

data class MovieWrapper @Inject constructor(
    val getListAllMovies: GetListAllMovies,
    val getListMovies: GetListMovies
)
