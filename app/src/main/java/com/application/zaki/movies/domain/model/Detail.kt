package com.application.zaki.movies.domain.model

data class Detail(
    val originalLanguage: String? = null,
    val title: String? = null,
    val backdropPath: String? = null,
    val cast: List<CastCrewItem>? = null,
    val crew: List<CastCrewItem>? = null,
    val genres: List<GenresItem>? = null,
    val id: Int? = null,
    val overview: String? = null,
    val posterPath: String? = null,
    val releaseDate: String? = null,
    val voteAverage: Float? = null,
    val videos: List<Videos>? = null,
    val runtime: Int? = null
)

data class CastCrewItem(
    val character: String? = null,
    val name: String? = null,
    val profilePath: String? = null,
    val id: Int? = null,
    val job: String? = null,
)

data class Videos(
    val id: String? = null,
    val key: String? = null
)

