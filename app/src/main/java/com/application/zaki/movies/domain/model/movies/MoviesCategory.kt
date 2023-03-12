package com.application.zaki.movies.domain.model.movies

import androidx.paging.PagingData

data class MoviesCategory(
    val moviesCategoryItem: MutableList<MoviesCategoryItem>? = null
)

data class MoviesCategoryItem(
    val categoryTitle: String? = null,
    val categories: PagingData<ListMovies>? = null
)