package com.application.zaki.movies.domain.model.movies

data class ListMovies(
    val overview: String? = null,
    val originalLanguage: String? = null,
    val originalTitle: String? = null,
    val video: Boolean? = null,
    val title: String? = null,
    val genreIds: List<Int?>? = null,
    val posterPath: String? = null,
    val backdropPath: String? = null,
    val releaseDate: String? = null,
    val popularity: Double? = null,
    val voteAverage: Double? = null,
    val id: Int? = null,
    val adult: Boolean? = null,
    val voteCount: Int? = null,
    val genres: List<GenreItemMovies>? = null
)

data class GenreItemMovies(
    val name: String? = null,
    val id: Int? = null
)