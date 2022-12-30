package com.application.zaki.movies.domain.model.movies

data class PopularMovies(
    val page: Int? = null,
    val results: List<ListPopularMovies?>? = null,
    val totalResults: Int? = null,
    val totalPages: Int? = null
)

data class ListPopularMovies(
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
    val genres: List<GenreItemPopularMovies?>? = null
)

data class GenreItemPopularMovies(
    val name: String? = null,
    val id: Int? = null
)
