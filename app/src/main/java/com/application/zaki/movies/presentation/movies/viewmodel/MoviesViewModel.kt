package com.application.zaki.movies.presentation.movies.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.LiveDataReactiveStreams
import androidx.lifecycle.ViewModel
import androidx.paging.PagingData
import com.application.zaki.movies.domain.model.movies.*
import com.application.zaki.movies.domain.interfaces.IMoviesUseCase
import com.application.zaki.movies.utils.NetworkResult
import com.application.zaki.movies.utils.RxDisposer
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MoviesViewModel @Inject constructor(private val moviesUseCase: IMoviesUseCase) : ViewModel() {
    fun popularMovies(rxDisposer: RxDisposer): LiveData<NetworkResult<PopularMovies>> =
        LiveDataReactiveStreams.fromPublisher(moviesUseCase.getPopularMovies(rxDisposer))

    fun nowPlayingMovies(rxDisposer: RxDisposer): LiveData<NetworkResult<NowPlayingMovies>> =
        LiveDataReactiveStreams.fromPublisher(moviesUseCase.getNowPlayingMovies(rxDisposer))

    fun topRatedMovies(rxDisposer: RxDisposer): LiveData<NetworkResult<TopRatedMovies>> =
        LiveDataReactiveStreams.fromPublisher(moviesUseCase.getTopRatedMovies(rxDisposer))

    fun upComingMovies(rxDisposer: RxDisposer): LiveData<NetworkResult<UpComingMovies>> =
        LiveDataReactiveStreams.fromPublisher(moviesUseCase.getUpComingMovies(rxDisposer))

    fun popularMoviesPaging(rxDisposer: RxDisposer): LiveData<NetworkResult<PagingData<ListPopularMovies>>> =
        LiveDataReactiveStreams.fromPublisher(moviesUseCase.getPopularMoviesPaging(rxDisposer))

    fun topRatedMoviesPaging(rxDisposer: RxDisposer): LiveData<NetworkResult<PagingData<ListTopRatedMovies>>> =
        LiveDataReactiveStreams.fromPublisher(moviesUseCase.getTopRatedMoviesPaging(rxDisposer))

    fun upComingMoviesPaging(rxDisposer: RxDisposer): LiveData<NetworkResult<PagingData<ListUpComingMovies>>> =
        LiveDataReactiveStreams.fromPublisher(moviesUseCase.getUpComingMoviesPaging(rxDisposer))
}