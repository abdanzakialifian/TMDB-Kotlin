package com.application.tmdb.core.domain.model

import androidx.paging.PagingData
import com.application.tmdb.common.Category
import com.application.tmdb.common.Movie
import com.application.tmdb.common.TvShow

data class CategoryModel(
    val categoryId: Int? = null,
    val categoryTitle: String? = null,
    val categories: PagingData<MovieTvShowModel>? = null,
    val category: com.application.tmdb.common.Category? = null,
    val movie: com.application.tmdb.common.Movie? = null,
    val tvShow: com.application.tmdb.common.TvShow? = null
)