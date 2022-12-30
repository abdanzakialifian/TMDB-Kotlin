package com.application.zaki.movies.presentation.detail.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.LiveDataReactiveStreams
import androidx.lifecycle.ViewModel
import com.application.zaki.movies.domain.interfaces.IMoviesUseCase
import com.application.zaki.movies.domain.interfaces.ITvShowsUseCase
import com.application.zaki.movies.domain.model.movies.DetailMovies
import com.application.zaki.movies.domain.model.movies.ReviewsMovie
import com.application.zaki.movies.domain.model.tvshows.DetailTvShows
import com.application.zaki.movies.utils.NetworkResult
import com.application.zaki.movies.utils.RxDisposer
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val movieUseCase: IMoviesUseCase,
    private val tvShowsUseCase: ITvShowsUseCase,
) : ViewModel() {
    fun detailMovies(
        rxDisposer: RxDisposer,
        movieId: String,
    ): LiveData<NetworkResult<DetailMovies>> =
        LiveDataReactiveStreams.fromPublisher(movieUseCase.getDetailMovies(rxDisposer, movieId))

    fun detailTvShows(
        rxDisposer: RxDisposer,
        tvId: String,
    ): LiveData<NetworkResult<DetailTvShows>> =
        LiveDataReactiveStreams.fromPublisher(tvShowsUseCase.getDetailTvShows(rxDisposer, tvId))

    fun reviewsMovie(
        rxDisposer: RxDisposer,
        movieId: String
    ): LiveData<NetworkResult<ReviewsMovie>> = LiveDataReactiveStreams.fromPublisher(
        movieUseCase.getReviewsMoviePaging(
            rxDisposer,
            movieId
        )
    )
}