package com.application.tmdb.domain.interfaces

import androidx.paging.PagingData
import com.application.tmdb.domain.model.DetailModel
import com.application.tmdb.domain.model.MovieTvShowModel
import com.application.tmdb.utils.Movie
import com.application.tmdb.utils.Page
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