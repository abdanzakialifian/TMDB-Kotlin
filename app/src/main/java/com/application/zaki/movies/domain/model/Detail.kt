package com.application.zaki.movies.domain.model

data class Detail(
    val originalLanguage: String? = null,
    val title: String? = null,
    val backdropPath: String? = null,
    val cast: List<CastItem>? = null,
    val crew: List<CastItem>? = null,
    val genres: List<GenresItem>? = null,
    val popularity: Any? = null,
    val id: Int? = null,
    val voteCount: Int? = null,
    val overview: String? = null,
    val posterPath: String? = null,
    val releaseDate: String? = null,
    val voteAverage: Float? = null,
    val tagline: String? = null,
    val adult: Boolean? = null,
    val homepage: String? = null,
    val status: String? = null,
    val videos: List<Videos>? = null,
    val runtime: Int? = null
)

data class CastItem(
    val castId: Int? = null,
    val character: String? = null,
    val gender: Int? = null,
    val creditId: String? = null,
    val knownForDepartment: String? = null,
    val originalName: String? = null,
    val popularity: Any? = null,
    val name: String? = null,
    val profilePath: String? = null,
    val id: Int? = null,
    val adult: Boolean? = null,
    val order: Int? = null
)

data class Videos(
    val site: String? = null,
    val size: Int? = null,
    val iso31661: String? = null,
    val name: String? = null,
    val official: Boolean? = null,
    val id: String? = null,
    val type: String? = null,
    val publishedAt: String? = null,
    val iso6391: String? = null,
    val key: String? = null
)

