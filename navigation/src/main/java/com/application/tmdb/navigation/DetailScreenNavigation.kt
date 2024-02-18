package com.application.tmdb.navigation

interface DetailScreenNavigation {
    fun launchDetailScreen(detailScreenArguments: DetailScreenArguments)

    data class DetailScreenArguments(
        val id: Int = 0,
        val intentFrom: String = "",
    )
}