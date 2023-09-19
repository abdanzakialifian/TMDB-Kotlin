package com.application.zaki.movies.domain.model.tvshows

import com.application.zaki.movies.domain.model.other.GenresItem

data class ListTvShows(
    val originalLanguage: String? = null,
    val genreIds: List<Int>? = null,
    val posterPath: String? = null,
    val voteAverage: Double? = null,
    val originalName: String? = null,
    val name: String? = null,
    val id: Int? = null,
    val genres: List<GenresItem>? = null
)
