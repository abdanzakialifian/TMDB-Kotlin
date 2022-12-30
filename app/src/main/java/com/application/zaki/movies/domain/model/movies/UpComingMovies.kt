package com.application.zaki.movies.domain.model.movies

data class UpComingMovies(
    val dates: DatesUpComingMovies? = null,
    val page: Int? = null,
    val totalPages: Int? = null,
    val results: List<ListUpComingMovies?>? = null,
    val totalResults: Int? = null
)

data class DatesUpComingMovies(
    val maximum: String? = null,
    val minimum: String? = null
)

data class ListUpComingMovies(
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
    val genres: List<GenreItemUpComingMovies?>? = null
)

data class GenreItemUpComingMovies(
    val name: String? = null,
    val id: Int? = null
)