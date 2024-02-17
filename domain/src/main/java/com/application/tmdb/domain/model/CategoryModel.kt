package com.application.tmdb.domain.model

import androidx.paging.PagingData
import com.application.tmdb.core.utils.Category
import com.application.tmdb.core.utils.Movie
import com.application.tmdb.core.utils.TvShow

data class CategoryModel(
    val categoryId: Int? = null,
    val categoryTitle: String? = null,
    val categories: PagingData<MovieTvShowModel>? = null,
    val category: Category? = null,
    val movie: Movie? = null,
    val tvShow: TvShow? = null
)