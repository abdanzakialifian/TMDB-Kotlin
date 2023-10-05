package com.application.zaki.movies.domain.model

data class CastCrewModel(
    val id: Int? = null,
    val title: String? = null,
    val castCrews: List<CastCrewItemModel>? = null
)
