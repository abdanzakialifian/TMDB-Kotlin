package com.application.zaki.movies.domain.model

import androidx.paging.PagingData
import com.application.zaki.movies.utils.Category
import com.application.zaki.movies.utils.Movie
import com.application.zaki.movies.utils.TvShow

data class CategoryModel(
    val categoryId: Int? = null,
    val categoryTitle: String? = null,
    val categories: PagingData<MovieTvShowModel>? = null,
    val category: Category? = null,
    val movie: Movie? = null,
    val tvShow: TvShow? = null
)