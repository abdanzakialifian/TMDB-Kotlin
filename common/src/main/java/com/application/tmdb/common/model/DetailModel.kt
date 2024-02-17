package com.application.tmdb.common.model

data class DetailModel(
    val originalLanguage: String? = null,
    val title: String? = null,
    val backdropPath: String? = null,
    val cast: List<CastCrewItemModel>? = null,
    val crew: List<CastCrewItemModel>? = null,
    val genres: List<GenreItemModel>? = null,
    val id: Int? = null,
    val overview: String? = null,
    val posterPath: String? = null,
    val releaseDate: String? = null,
    val voteAverage: Float? = null,
    val videos: List<VideoItemModel>? = null,
    val runtime: Int? = null,
    val certification: String? = null
)

data class CastCrewItemModel(
    val id: Int? = null,
    val character: String? = null,
    val name: String? = null,
    val profilePath: String? = null,
    val job: String? = null,
)

data class VideoItemModel(
    val id: String? = null,
    val key: String? = null
)

