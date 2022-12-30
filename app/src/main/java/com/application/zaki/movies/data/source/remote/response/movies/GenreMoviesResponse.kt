package com.application.zaki.movies.data.source.remote.response.movies

import com.google.gson.annotations.SerializedName

data class GenreMoviesResponse(

	@field:SerializedName("genres")
	val genres: List<GenresItemMoviesResponse?>? = null
)

data class GenresItemMoviesResponse(

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("id")
	val id: Int? = null
)
