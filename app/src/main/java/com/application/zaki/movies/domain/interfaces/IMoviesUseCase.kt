package com.application.zaki.movies.domain.interfaces

import androidx.paging.PagingData
import com.application.zaki.movies.domain.model.movies.*
import com.application.zaki.movies.utils.Genre
import com.application.zaki.movies.utils.Movie
import com.application.zaki.movies.utils.Page
import io.reactivex.Flowable

interface IMoviesUseCase {
    fun getDetailMovies(movieId: String): Flowable<DetailMovies>
    fun getMovies(
        movie: Movie,
        genre: Genre,
        page: Page
    ): Flowable<PagingData<ListMovies>>
}