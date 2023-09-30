package com.application.zaki.movies.data.source.remote.response.tvshows

import com.application.zaki.movies.data.source.remote.response.other.ReviewsResponse
import com.google.gson.annotations.SerializedName

data class DetailTvShowsResponse(

    @field:SerializedName("original_language")
    val originalLanguage: String? = null,

    @field:SerializedName("number_of_episodes")
    val numberOfEpisodes: Int? = null,

    @field:SerializedName("videos")
    val videos: VideosResponse? = null,

    @field:SerializedName("networks")
    val networks: List<NetworksItemResponse>? = null,

    @field:SerializedName("type")
    val type: String? = null,

    @field:SerializedName("backdrop_path")
    val backdropPath: String? = null,

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

    @field:SerializedName("number_of_seasons")
    val numberOfSeasons: Int? = null,

    @field:SerializedName("vote_count")
    val voteCount: Int? = null,

    @field:SerializedName("first_air_date")
    val firstAirDate: String? = null,

    @field:SerializedName("overview")
    val overview: String? = null,

    @field:SerializedName("seasons")
    val seasons: List<SeasonsItemResponse>? = null,

    @field:SerializedName("languages")
    val languages: List<String>? = null,

    @field:SerializedName("created_by")
    val createdBy: List<Any>? = null,

    @field:SerializedName("last_episode_to_air")
    val lastEpisodeToAir: LastEpisodeToAirResponse? = null,

    @field:SerializedName("poster_path")
    val posterPath: String? = null,

    @field:SerializedName("origin_country")
    val originCountry: List<String>? = null,

    @field:SerializedName("spoken_languages")
    val spokenLanguages: List<SpokenLanguagesItemResponse>? = null,

    @field:SerializedName("production_companies")
    val productionCompanies: List<ProductionCompaniesItemResponse>? = null,

    @field:SerializedName("original_name")
    val originalName: String? = null,

    @field:SerializedName("vote_average")
    val voteAverage: Float? = null,

    @field:SerializedName("name")
    val name: String? = null,

    @field:SerializedName("tagline")
    val tagline: String? = null,

    @field:SerializedName("episode_run_time")
    val episodeRunTime: List<Any>? = null,

    @field:SerializedName("adult")
    val adult: Boolean? = null,

    @field:SerializedName("next_episode_to_air")
    val nextEpisodeToAir: Any? = null,

    @field:SerializedName("in_production")
    val inProduction: Boolean? = null,

    @field:SerializedName("last_air_date")
    val lastAirDate: String? = null,

    @field:SerializedName("homepage")
    val homepage: String? = null,

    @field:SerializedName("status")
    val status: String? = null,

    @field:SerializedName("reviews")
    val reviews: ReviewsResponse? = null
)

data class LastEpisodeToAirResponse(

    @field:SerializedName("production_code")
    val productionCode: String? = null,

    @field:SerializedName("air_date")
    val airDate: String? = null,

    @field:SerializedName("overview")
    val overview: String? = null,

    @field:SerializedName("episode_number")
    val episodeNumber: Int? = null,

    @field:SerializedName("show_id")
    val showId: Int? = null,

    @field:SerializedName("vote_average")
    val voteAverage: Any? = null,

    @field:SerializedName("name")
    val name: String? = null,

    @field:SerializedName("season_number")
    val seasonNumber: Int? = null,

    @field:SerializedName("runtime")
    val runtime: Int? = null,

    @field:SerializedName("id")
    val id: Int? = null,

    @field:SerializedName("still_path")
    val stillPath: String? = null,

    @field:SerializedName("vote_count")
    val voteCount: Int? = null
)

data class GenresItemResponse(

    @field:SerializedName("name")
    val name: String? = null,

    @field:SerializedName("id")
    val id: Int? = null
)

data class CastItemResponse(

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

data class CreditsResponse(

    @field:SerializedName("cast")
    val cast: List<CastItemResponse>? = null,

    @field:SerializedName("crew")
    val crew: List<CrewItemResponse>? = null
)

data class SeasonsItemResponse(

    @field:SerializedName("air_date")
    val airDate: String? = null,

    @field:SerializedName("overview")
    val overview: String? = null,

    @field:SerializedName("episode_count")
    val episodeCount: Int? = null,

    @field:SerializedName("name")
    val name: String? = null,

    @field:SerializedName("season_number")
    val seasonNumber: Int? = null,

    @field:SerializedName("id")
    val id: Int? = null,

    @field:SerializedName("poster_path")
    val posterPath: String? = null
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

data class NetworksItemResponse(

    @field:SerializedName("logo_path")
    val logoPath: String? = null,

    @field:SerializedName("name")
    val name: String? = null,

    @field:SerializedName("id")
    val id: Int? = null,

    @field:SerializedName("origin_country")
    val originCountry: String? = null
)

data class SpokenLanguagesItemResponse(

    @field:SerializedName("name")
    val name: String? = null,

    @field:SerializedName("iso_639_1")
    val iso6391: String? = null,

    @field:SerializedName("english_name")
    val englishName: String? = null
)

data class ProductionCountriesItemResponse(

    @field:SerializedName("iso_3166_1")
    val iso31661: String? = null,

    @field:SerializedName("name")
    val name: String? = null
)

data class VideosResponse(

    @field:SerializedName("results")
    val results: List<ResultsItemResponse>? = null
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
    val profilePath: Any? = null,

    @field:SerializedName("id")
    val id: Int? = null,

    @field:SerializedName("adult")
    val adult: Boolean? = null,

    @field:SerializedName("department")
    val department: String? = null,

    @field:SerializedName("job")
    val job: String? = null
)
