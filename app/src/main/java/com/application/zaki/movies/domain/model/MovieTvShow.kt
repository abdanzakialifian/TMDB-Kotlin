package com.application.zaki.movies.domain.model

data class MovieTvShow(
    val name: String? = null,
    val genreIds: List<Int>? = null,
    val posterPath: String? = null,
    val voteAverage: Double? = null,
    val id: Int? = null,
    val overview: String? = null,
    val releaseDate: String? = null,
    val backdropPath: String? = null
)