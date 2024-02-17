package com.application.tmdb.data.source.remote.response.other

import com.google.gson.annotations.SerializedName

data class DetailCastResponse(

	@field:SerializedName("also_known_as")
	val alsoKnownAs: List<String>? = null,

	@field:SerializedName("birthday")
	val birthday: String? = null,

	@field:SerializedName("gender")
	val gender: Int? = null,

	@field:SerializedName("imdb_id")
	val imdbId: String? = null,

	@field:SerializedName("known_for_department")
	val knownForDepartment: String? = null,

	@field:SerializedName("profile_path")
	val profilePath: String? = null,

	@field:SerializedName("biography")
	val biography: String? = null,

	@field:SerializedName("deathday")
	val deathday: String? = null,

	@field:SerializedName("place_of_birth")
	val placeOfBirth: String? = null,

	@field:SerializedName("movie_credits")
	val movieCredits: MovieCredits? = null,

	@field:SerializedName("popularity")
	val popularity: Any? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("adult")
	val adult: Boolean? = null,

	@field:SerializedName("homepage")
	val homepage: String? = null
)

data class CastItemResponse(

	@field:SerializedName("overview")
	val overview: String? = null,

	@field:SerializedName("original_language")
	val originalLanguage: String? = null,

	@field:SerializedName("original_title")
	val originalTitle: String? = null,

	@field:SerializedName("video")
	val video: Boolean? = null,

	@field:SerializedName("title")
	val title: String? = null,

	@field:SerializedName("genre_ids")
	val genreIds: List<Int>? = null,

	@field:SerializedName("poster_path")
	val posterPath: String? = null,

	@field:SerializedName("backdrop_path")
	val backdropPath: String? = null,

	@field:SerializedName("character")
	val character: String? = null,

	@field:SerializedName("release_date")
	val releaseDate: String? = null,

	@field:SerializedName("credit_id")
	val creditId: String? = null,

	@field:SerializedName("popularity")
	val popularity: Any? = null,

	@field:SerializedName("vote_average")
	val voteAverage: Any? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("adult")
	val adult: Boolean? = null,

	@field:SerializedName("vote_count")
	val voteCount: Int? = null,

	@field:SerializedName("order")
	val order: Int? = null
)

data class CrewItemResponse(

	@field:SerializedName("overview")
	val overview: String? = null,

	@field:SerializedName("original_language")
	val originalLanguage: String? = null,

	@field:SerializedName("original_title")
	val originalTitle: String? = null,

	@field:SerializedName("video")
	val video: Boolean? = null,

	@field:SerializedName("title")
	val title: String? = null,

	@field:SerializedName("genre_ids")
	val genreIds: List<Int>? = null,

	@field:SerializedName("poster_path")
	val posterPath: String? = null,

	@field:SerializedName("backdrop_path")
	val backdropPath: String? = null,

	@field:SerializedName("release_date")
	val releaseDate: String? = null,

	@field:SerializedName("credit_id")
	val creditId: String? = null,

	@field:SerializedName("popularity")
	val popularity: Any? = null,

	@field:SerializedName("vote_average")
	val voteAverage: Any? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("adult")
	val adult: Boolean? = null,

	@field:SerializedName("department")
	val department: String? = null,

	@field:SerializedName("job")
	val job: String? = null,

	@field:SerializedName("vote_count")
	val voteCount: Int? = null
)

data class MovieCredits(

	@field:SerializedName("cast")
	val cast: List<CastItemResponse>? = null,

	@field:SerializedName("crew")
	val crew: List<CrewItemResponse>? = null
)
