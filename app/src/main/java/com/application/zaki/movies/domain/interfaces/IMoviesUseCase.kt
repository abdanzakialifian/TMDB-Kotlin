package com.application.zaki.movies.domain.interfaces

import androidx.paging.PagingData
import com.application.zaki.movies.domain.model.movies.*
import io.reactivex.Flowable

interface IMoviesUseCase {
    fun getNowPlayingMovies(): Flowable<NowPlayingMovies>
    fun getDetailMovies(movieId: String): Flowable<DetailMovies>
    fun getPopularMoviesPaging(
        type: String,
        totalPage: String
    ): Flowable<PagingData<ListPopularMovies>>

    fun getTopRatedMoviesPaging(
        type: String,
        totalPage: String
    ): Flowable<PagingData<ListTopRatedMovies>>

    fun getUpComingMoviesPaging(
        type: String,
        totalPage: String
    ): Flowable<PagingData<ListUpComingMovies>>
}