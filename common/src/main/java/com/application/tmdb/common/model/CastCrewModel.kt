package com.application.tmdb.common.model

import com.application.tmdb.common.utils.Category

data class CastCrewModel(
    val id: Int? = null,
    val title: String? = null,
    val castCrews: List<CastCrewItemModel>? = null,
    val category: Category
)
