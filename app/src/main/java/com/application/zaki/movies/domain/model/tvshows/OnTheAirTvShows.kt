package com.application.zaki.movies.domain.model.tvshows

data class OnTheAirTvShows(
    val page: Int? = null,
    val totalPages: Int? = null,
    val results: List<ListOnTheAirTvShows?>? = null,
    val totalResults: Int? = null
)

data class ListOnTheAirTvShows(
    val firstAirDate: String? = null,
    val overview: String? = null,
    val originalLanguage: String? = null,
    val genreIds: List<Int?>? = null,
    val posterPath: String? = null,
    val originCountry: List<String?>? = null,
    val backdropPath: String? = null,
    val popularity: Double? = null,
    val voteAverage: Double? = null,
    val originalName: String? = null,
    val name: String? = null,
    val id: Int? = null,
    val voteCount: Int? = null,
    val genres: List<GenresItemOnTheAirTvShows?>? = null
)

data class GenresItemOnTheAirTvShows(
    val name: String? = null,
    val id: Int? = null
)
