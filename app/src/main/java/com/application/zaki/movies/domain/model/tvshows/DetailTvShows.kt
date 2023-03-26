package com.application.zaki.movies.domain.model.tvshows

import com.application.zaki.movies.domain.model.genre.GenresItem

data class DetailTvShows(
    val originalLanguage: String? = null,
    val numberOfEpisodes: Int? = null,
    val videos: Videos? = null,
    val networks: List<NetworksItem>? = null,
    val type: String? = null,
    val backdropPath: String? = null,
    val credits: Credits? = null,
    val genres: List<GenresItem>? = null,
    val popularity: Any? = null,
    val productionCountries: List<ProductionCountriesItem>? = null,
    val id: Int? = null,
    val numberOfSeasons: Int? = null,
    val voteCount: Int? = null,
    val firstAirDate: String? = null,
    val overview: String? = null,
    val seasons: List<SeasonsItem?>? = null,
    val languages: List<String>? = null,
    val createdBy: List<Any>? = null,
    val lastEpisodeToAir: LastEpisodeToAir? = null,
    val posterPath: String? = null,
    val originCountry: List<String?>? = null,
    val spokenLanguages: List<SpokenLanguagesItem>? = null,
    val productionCompanies: List<ProductionCompaniesItem>? = null,
    val originalName: String? = null,
    val voteAverage: Any? = null,
    val name: String? = null,
    val tagline: String? = null,
    val episodeRunTime: List<Any>? = null,
    val adult: Boolean? = null,
    val nextEpisodeToAir: Any? = null,
    val inProduction: Boolean? = null,
    val lastAirDate: String? = null,
    val homepage: String? = null,
    val status: String? = null
)

data class ProductionCompaniesItem(
    val logoPath: String? = null,
    val name: String? = null,
    val id: Int? = null,
    val originCountry: String? = null
)

data class ResultsItem(
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

data class NetworksItem(
    val logoPath: String? = null,
    val name: String? = null,
    val id: Int? = null,
    val originCountry: String? = null
)

data class Credits(
    val cast: List<CastItem>? = null,
    val crew: List<CrewItem>? = null
)

data class SeasonsItem(
    val airDate: String? = null,
    val overview: String? = null,
    val episodeCount: Int? = null,
    val name: String? = null,
    val seasonNumber: Int? = null,
    val id: Int? = null,
    val posterPath: String? = null
)

data class Videos(
    val results: List<ResultsItem>? = null
)

data class CastItem(
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

data class SpokenLanguagesItem(
    val name: String? = null,
    val iso6391: String? = null,
    val englishName: String? = null
)

data class CrewItem(
    val gender: Int? = null,
    val creditId: String? = null,
    val knownForDepartment: String? = null,
    val originalName: String? = null,
    val popularity: Any? = null,
    val name: String? = null,
    val profilePath: Any? = null,
    val id: Int? = null,
    val adult: Boolean? = null,
    val department: String? = null,
    val job: String? = null
)

data class ProductionCountriesItem(
    val iso31661: String? = null,
    val name: String? = null
)

data class LastEpisodeToAir(
    val productionCode: String? = null,
    val airDate: String? = null,
    val overview: String? = null,
    val episodeNumber: Int? = null,
    val showId: Int? = null,
    val voteAverage: Any? = null,
    val name: String? = null,
    val seasonNumber: Int? = null,
    val runtime: Int? = null,
    val id: Int? = null,
    val stillPath: String? = null,
    val voteCount: Int? = null
)
