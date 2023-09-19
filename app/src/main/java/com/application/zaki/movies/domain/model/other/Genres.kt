package com.application.zaki.movies.domain.model.other

data class Genres(
    val genres: List<GenresItem>? = null
)

data class GenresItem(
    val name: String? = null,
    val id: Int? = null
)
