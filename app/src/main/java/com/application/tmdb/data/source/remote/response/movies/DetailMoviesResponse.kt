package com.application.tmdb.data.source.remote.response.movies

import com.application.tmdb.data.source.remote.response.other.ReviewsResponse
import com.google.gson.annotations.SerializedName

data class DetailMoviesResponse(

	@field:SerializedName("original_language")
	val originalLanguage: String? = null,

	@field:SerializedName("imdb_id")
	val imdbId: String? = null,

	@field:SerializedName("videos")
	val videos: VideosResponse? = null,

	@field:SerializedName("video")
	val video: Boolean? = null,

	@field:SerializedName("title")
	val title: String? = null,

	@field:SerializedName("backdrop_path")
	val backdropPath: String? = null,

	@field:SerializedName("revenue")
	val revenue: Int? = null,

	@field:SerializedName("credits")
	val credits: CreditsResponse? = null,

	@field:SerializedName("genres")
	val genres: List<GenresItemResponse>? = null,

	@field:SerializedName("popularity")
	val popularity: Any? = null,

	@field:SerializedName("production_countries")
	val productionCountries: List<ProductionCountriesItemResponse>? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("vote_count")
	val voteCount: Int? = null,

	@field:SerializedName("budget")
	val budget: Int? = null,

	@field:SerializedName("overview")
	val overview: String? = null,

	@field:SerializedName("original_title")
	val originalTitle: String? = null,

	@field:SerializedName("runtime")
	val runtime: Int? = null,

	@field:SerializedName("poster_path")
	val posterPath: String? = null,

	@field:SerializedName("spoken_languages")
	val spokenLanguages: List<SpokenLanguagesItemResponse>? = null,

	@field:SerializedName("production_companies")
	val productionCompanies: List<ProductionCompaniesItemResponse>? = null,

	@field:SerializedName("release_date")
	val releaseDate: String? = null,

	@field:SerializedName("vote_average")
	val voteAverage: Float? = null,

	@field:SerializedName("belongs_to_collection")
	val belongsToCollection: BelongsToCollectionResponse? = null,

	@field:SerializedName("tagline")
	val tagline: String? = null,

	@field:SerializedName("adult")
	val adult: Boolean? = null,

	@field:SerializedName("homepage")
	val homepage: String? = null,

	@field:SerializedName("status")
	val status: String? = null,

	@field:SerializedName("reviews")
	val reviews: ReviewsResponse? = null,

	@field:SerializedName("release_dates")
	val releaseDates: ReleaseDateResponse? = null
)

data class CrewItemResponse(

	@field:SerializedName("gender")
	val gender: Int? = null,

	@field:SerializedName("credit_id")
	val creditId: String? = null,

	@field:SerializedName("known_for_department")
	val knownForDepartment: String? = null,

	@field:SerializedName("original_name")
	val originalName: String? = null,

	@field:SerializedName("popularity")
	val popularity: Any? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("profile_path")
	val profilePath: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("adult")
	val adult: Boolean? = null,

	@field:SerializedName("department")
	val department: String? = null,

	@field:SerializedName("job")
	val job: String? = null
)

data class ProductionCompaniesItemResponse(

	@field:SerializedName("logo_path")
	val logoPath: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("origin_country")
	val originCountry: String? = null
)

data class VideosResponse(

	@field:SerializedName("results")
	val results: List<ResultsItemResponse>? = null
)

data class CreditsResponse(

	@field:SerializedName("cast")
	val cast: List<CastItemResponse>? = null,

	@field:SerializedName("crew")
	val crew: List<CrewItemResponse>? = null
)

data class CastItemResponse(

	@field:SerializedName("cast_id")
	val castId: Int? = null,

	@field:SerializedName("character")
	val character: String? = null,

	@field:SerializedName("gender")
	val gender: Int? = null,

	@field:SerializedName("credit_id")
	val creditId: String? = null,

	@field:SerializedName("known_for_department")
	val knownForDepartment: String? = null,

	@field:SerializedName("original_name")
	val originalName: String? = null,

	@field:SerializedName("popularity")
	val popularity: Any? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("profile_path")
	val profilePath: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("adult")
	val adult: Boolean? = null,

	@field:SerializedName("order")
	val order: Int? = null
)

data class GenresItemResponse(

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("id")
	val id: Int? = null
)

data class SpokenLanguagesItemResponse(

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("iso_639_1")
	val iso6391: String? = null,

	@field:SerializedName("english_name")
	val englishName: String? = null
)

data class ResultsItemResponse(

	@field:SerializedName("site")
	val site: String? = null,

	@field:SerializedName("size")
	val size: Int? = null,

	@field:SerializedName("iso_3166_1")
	val iso31661: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("official")
	val official: Boolean? = null,

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("type")
	val type: String? = null,

	@field:SerializedName("published_at")
	val publishedAt: String? = null,

	@field:SerializedName("iso_639_1")
	val iso6391: String? = null,

	@field:SerializedName("key")
	val key: String? = null
)

data class ProductionCountriesItemResponse(

	@field:SerializedName("iso_3166_1")
	val iso31661: String? = null,

	@field:SerializedName("name")
	val name: String? = null
)

data class BelongsToCollectionResponse(

	@field:SerializedName("backdrop_path")
	val backdropPath: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("poster_path")
	val posterPath: String? = null
)

data class ReleaseDateResponse(
	@field:SerializedName("results")
	val results: List<ReleaseDateResultItemResponse>
)

data class ReleaseDateResultItemResponse(
	@field:SerializedName("iso_3166_1")
	val iso31661: String? = null,

	@field:SerializedName("release_dates")
	val releaseDates: List<ReleaseDateItemResponse>? = null
)

data class ReleaseDateItemResponse(
	@field:SerializedName("certification")
	val certification: String? = null,

	@field:SerializedName("descriptors")
	val descriptors: List<Any>? = null,

	@field:SerializedName("iso_639_1")
	val iso6391: String? = null,

	@field:SerializedName("note")
	val note: String? = null,

	@field:SerializedName("release_date")
	val releaseDate: String? = null,

	@field:SerializedName("type")
	val type: Int? = null,
)
