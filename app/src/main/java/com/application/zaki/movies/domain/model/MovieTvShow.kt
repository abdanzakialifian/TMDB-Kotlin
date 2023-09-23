package com.application.zaki.movies.domain.model

import com.application.zaki.movies.domain.model.GenresItem

data class MovieTvShow(
    val name: String? = null,
    val genreIds: List<Int>? = null,
    val posterPath: String? = null,
    val voteAverage: Double? = null,
    val id: Int? = null,
    val genres: List<GenresItem>? = null
)