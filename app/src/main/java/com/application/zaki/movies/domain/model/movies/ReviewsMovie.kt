package com.application.zaki.movies.domain.model.movies

data class ReviewsMovie(
    val id: Int? = null,
    val page: Int? = null,
    val totalPages: Int? = null,
    val results: ArrayList<ReviewItem>? = null,
    val totalResults: Int? = null
)

data class AuthorDetails(
    val avatarPath: Any? = null,
    val name: String? = null,
    val rating: Any? = null,
    val username: String? = null
)

data class ReviewItem(
    val authorDetails: AuthorDetails? = null,
    val updatedAt: String? = null,
    val author: String? = null,
    val createdAt: String? = null,
    val id: String? = null,
    val content: String? = null,
    val url: String? = null
)
