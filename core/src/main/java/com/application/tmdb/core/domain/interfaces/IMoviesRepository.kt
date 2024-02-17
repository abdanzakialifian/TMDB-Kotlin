package com.application.tmdb.core.domain.interfaces

import androidx.paging.PagingData
import com.application.tmdb.core.domain.model.DetailModel
import com.application.tmdb.core.domain.model.MovieTvShowModel
import com.application.tmdb.core.utils.Movie
import com.application.tmdb.core.utils.Page
import io.reactivex.Flowable

interface IMoviesRepository {
    fun getDetailMovies(movieId: String): Flowable<DetailModel>

    fun getMovies(
        movie: Movie?,
        page: Page?,
        query: String?,
        movieId: Int?
    ): Flowable<PagingData<MovieTvShowModel>>
}