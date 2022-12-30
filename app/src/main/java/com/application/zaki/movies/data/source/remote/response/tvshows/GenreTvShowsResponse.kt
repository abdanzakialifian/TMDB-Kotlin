package com.application.zaki.movies.data.source.remote.response.tvshows

import com.google.gson.annotations.SerializedName

data class GenreTvShowsResponse(

	@field:SerializedName("genres")
	val genres: List<GenresItemTvShowsResponse?>? = null
)

data class GenresItemTvShowsResponse(

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("id")
	val id: Int? = null
)
