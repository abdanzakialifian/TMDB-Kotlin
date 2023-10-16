package com.application.zaki.movies.domain.model

import com.application.zaki.movies.utils.Category

data class CastCrewModel(
    val id: Int? = null,
    val title: String? = null,
    val castCrews: List<CastCrewItemModel>? = null,
    val category: Category
)
