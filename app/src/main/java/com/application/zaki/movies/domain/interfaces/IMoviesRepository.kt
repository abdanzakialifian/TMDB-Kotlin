package com.application.zaki.movies.domain.interfaces

import androidx.paging.PagingData
import com.application.zaki.movies.domain.model.movies.*
import com.application.zaki.movies.utils.NetworkResult
import com.application.zaki.movies.utils.RxDisposer
import io.reactivex.Flowable

interface IMoviesRepository {
    fun getPopularMovies(rxDisposer: RxDisposer): Flowable<NetworkResult<PopularMovies>>
    fun getNowPlayingMovies(rxDisposer: RxDisposer): Flowable<NetworkResult<NowPlayingMovies>>
    fun getTopRatedMovies(rxDisposer: RxDisposer): Flowable<NetworkResult<TopRatedMovies>>
    fun getUpComingMovies(rxDisposer: RxDisposer): Flowable<NetworkResult<UpComingMovies>>
    fun getDetailMovies(
        rxDisposer: RxDisposer,
        movieId: String
    ): Flowable<NetworkResult<DetailMovies>>

    fun getPopularMoviesPaging(rxDisposer: RxDisposer): Flowable<NetworkResult<PagingData<ListPopularMovies>>>
    fun getTopRatedMoviesPaging(rxDisposer: RxDisposer): Flowable<NetworkResult<PagingData<ListTopRatedMovies>>>
    fun getUpComingMoviesPaging(rxDisposer: RxDisposer): Flowable<NetworkResult<PagingData<ListUpComingMovies>>>
    fun getReviewsMoviePaging(
        rxDisposer: RxDisposer,
        movieId: String
    ): Flowable<NetworkResult<ReviewsMovie>>

    fun getDiscoverMovies(
        rxDisposer: RxDisposer,
        genreId: String
    ): Flowable<NetworkResult<PagingData<ResultsItemDiscover>>>
}