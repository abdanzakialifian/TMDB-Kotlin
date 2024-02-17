package com.application.tmdb.core.domain.model

import com.application.tmdb.common.Category

data class CastCrewModel(
    val id: Int? = null,
    val title: String? = null,
    val castCrews: List<CastCrewItemModel>? = null,
    val category: com.application.tmdb.common.Category
)
