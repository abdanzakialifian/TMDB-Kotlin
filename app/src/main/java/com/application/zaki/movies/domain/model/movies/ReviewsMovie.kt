package com.application.zaki.movies.domain.model.movies

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
