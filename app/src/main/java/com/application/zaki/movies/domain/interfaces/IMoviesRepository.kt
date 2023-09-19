package com.application.zaki.movies.domain.interfaces

import androidx.paging.PagingData
import com.application.zaki.movies.domain.model.other.Genres
import com.application.zaki.movies.domain.model.movies.DetailMovies
import com.application.zaki.movies.domain.model.movies.ListMovies
import com.application.zaki.movies.utils.Category
import com.application.zaki.movies.utils.Movie
import com.application.zaki.movies.utils.Page
import io.reactivex.Flowable

interface IMoviesRepository {
    fun getDetailMovies(movieId: String): Flowable<DetailMovies>
    fun getGenres(category: Category): Flowable<Genres>
    fun getMovies(
        movie: Movie,
        page: Page
    ): Flowable<PagingData<ListMovies>>
}