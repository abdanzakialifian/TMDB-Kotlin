package com.application.tmdb.domain.interfaces

import androidx.paging.PagingData
import com.application.tmdb.common.model.DetailModel
import com.application.tmdb.common.model.MovieTvShowModel
import com.application.tmdb.common.utils.Movie
import com.application.tmdb.common.utils.Page
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