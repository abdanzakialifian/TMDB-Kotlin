package com.application.zaki.movies.domain.interfaces

import androidx.paging.PagingData
import com.application.zaki.movies.domain.model.movies.*
import com.application.zaki.movies.utils.UiState
import com.application.zaki.movies.utils.RxDisposer
import io.reactivex.Flowable

interface IMoviesRepository {
    fun getPopularMovies(): Flowable<PopularMovies>
    fun getNowPlayingMovies(): Flowable<NowPlayingMovies>
    fun getTopRatedMovies(): Flowable<TopRatedMovies>
    fun getUpComingMovies(): Flowable<UpComingMovies>
    fun getDetailMovies(
        rxDisposer: RxDisposer,
        movieId: String
    ): Flowable<UiState<DetailMovies>>

    fun getPopularMoviesPaging(): Flowable<PagingData<ListPopularMovies>>
    fun getTopRatedMoviesPaging(): Flowable<PagingData<ListTopRatedMovies>>
    fun getUpComingMoviesPaging(): Flowable<PagingData<ListUpComingMovies>>
    fun getReviewsMoviePaging(
        rxDisposer: RxDisposer,
        movieId: String
    ): Flowable<UiState<ReviewsMovie>>

    fun getDiscoverMovies(
        rxDisposer: RxDisposer,
        genreId: String
    ): Flowable<UiState<PagingData<ResultsItemDiscover>>>
}