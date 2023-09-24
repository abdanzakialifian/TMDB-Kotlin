package com.application.zaki.movies.domain.interfaces

import androidx.paging.PagingData
import com.application.zaki.movies.domain.model.MovieTvShow
import com.application.zaki.movies.domain.model.Detail
import com.application.zaki.movies.domain.model.Genres
import com.application.zaki.movies.utils.Category
import com.application.zaki.movies.utils.Movie
import com.application.zaki.movies.utils.Page
import io.reactivex.Flowable

interface IMoviesRepository {
    fun getDetailMovies(movieId: String): Flowable<Detail>

    fun getMovies(
        movie: Movie,
        page: Page
    ): Flowable<PagingData<MovieTvShow>>
}