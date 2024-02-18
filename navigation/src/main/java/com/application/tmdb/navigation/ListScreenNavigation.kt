package com.application.tmdb.navigation

import com.application.tmdb.common.utils.Movie
import com.application.tmdb.common.utils.TvShow

interface ListScreenNavigation {
    fun launchListScreen(listScreenArguments: ListScreenArguments)

    data class ListScreenArguments(
        val intentFrom: String = "",
        val movie: Movie = Movie.POPULAR_MOVIES,
        val tvShow: TvShow = TvShow.POPULAR_TV_SHOWS,
    )
}