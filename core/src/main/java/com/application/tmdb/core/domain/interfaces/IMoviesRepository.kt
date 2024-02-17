package com.application.tmdb.core.domain.interfaces

import androidx.paging.PagingData
import com.application.tmdb.core.domain.model.DetailModel
import com.application.tmdb.core.domain.model.MovieTvShowModel
import com.application.tmdb.common.Movie
import com.application.tmdb.common.Page
import io.reactivex.Flowable

interface IMoviesRepository {
    fun getDetailMovies(movieId: String): Flowable<DetailModel>

    fun getMovies(
        movie: com.application.tmdb.common.Movie?,
        page: com.application.tmdb.common.Page?,
        query: String?,
        movieId: Int?
    ): Flowable<PagingData<MovieTvShowModel>>
}