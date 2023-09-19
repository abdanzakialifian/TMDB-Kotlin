package com.application.zaki.movies.domain.model.movies

import com.application.zaki.movies.domain.model.other.GenresItem
import com.application.zaki.movies.domain.model.other.ReviewItem

data class DetailMovies(
    val originalLanguage: String? = null,
    val imdbId: String? = null,
    val videos: Videos? = null,
    val video: Boolean? = null,
    val title: String? = null,
    val backdropPath: String? = null,
    val revenue: Int? = null,
    val credits: Credits? = null,
    val genres: List<GenresItem>? = null,
    val popularity: Any? = null,
    val productionCountries: List<ProductionCountriesItem>? = null,
    val id: Int? = null,
    val voteCount: Int? = null,
    val budget: Int? = null,
    val overview: String? = null,
    val originalTitle: String? = null,
    val runtime: Int? = null,
    val posterPath: String? = null,
    val spokenLanguages: List<SpokenLanguagesItem>? = null,
    val productionCompanies: List<ProductionCompaniesItem>? = null,
    val releaseDate: String? = null,
    val voteAverage: Any? = null,
    val belongsToCollection: BelongsToCollection? = null,
    val tagline: String? = null,
    val adult: Boolean? = null,
    val homepage: String? = null,
    val status: String? = null,
    val reviews: List<ReviewItem>? = null
)

data class CrewItem(
    val gender: Int? = null,
    val creditId: String? = null,
    val knownForDepartment: String? = null,
    val originalName: String? = null,
    val popularity: Any? = null,
    val name: String? = null,
    val profilePath: String? = null,
    val id: Int? = null,
    val adult: Boolean? = null,
    val department: String? = null,
    val job: String? = null
)

data class ProductionCompaniesItem(
    val logoPath: String? = null,
    val name: String? = null,
    val id: Int? = null,
    val originCountry: String? = null
)

data class Videos(
    val results: List<ResultsItem>? = null
)

data class Credits(
    val cast: List<CastItem>? = null,
    val crew: List<CrewItem>? = null
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

data class SpokenLanguagesItem(
    val name: String? = null,
    val iso6391: String? = null,
    val englishName: String? = null
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

data class ProductionCountriesItem(
    val iso31661: String? = null,
    val name: String? = null
)

data class BelongsToCollection(
    val backdropPath: String? = null,
    val name: String? = null,
    val id: Int? = null,
    val posterPath: String? = null
)

