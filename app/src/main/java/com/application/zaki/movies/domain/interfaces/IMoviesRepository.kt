package com.application.zaki.movies.domain.interfaces

import androidx.paging.PagingData
import com.application.zaki.movies.domain.model.DetailModel
import com.application.zaki.movies.domain.model.MovieTvShowModel
import com.application.zaki.movies.utils.Movie
import com.application.zaki.movies.utils.Page
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