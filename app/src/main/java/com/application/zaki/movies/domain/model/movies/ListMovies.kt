package com.application.zaki.movies.domain.model.movies

import com.application.zaki.movies.domain.model.genre.GenresItem

data class ListMovies(
    val originalTitle: String? = null,
    val title: String? = null,
    val genreIds: List<Int>? = null,
    val posterPath: String? = null,
    val voteAverage: Double? = null,
    val id: Int? = null,
    val genres: List<GenresItem>? = null
)