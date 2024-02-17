package com.application.tmdb.core.domain.model

import com.application.tmdb.core.utils.Category

data class CastCrewModel(
    val id: Int? = null,
    val title: String? = null,
    val castCrews: List<CastCrewItemModel>? = null,
    val category: Category
)
