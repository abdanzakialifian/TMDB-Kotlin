package com.application.zaki.movies.presentation.tvshows.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.LiveDataReactiveStreams
import androidx.lifecycle.ViewModel
import androidx.paging.PagingData
import com.application.zaki.movies.domain.model.tvshows.*
import com.application.zaki.movies.domain.interfaces.ITvShowsUseCase
import com.application.zaki.movies.utils.UiState
import com.application.zaki.movies.utils.RxDisposer
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TvShowsViewModel @Inject constructor(private val tvShowsUseCase: ITvShowsUseCase) :
    ViewModel() {

    fun airingTodayTvShows(rxDisposer: RxDisposer): LiveData<UiState<AiringTodayTvShows>> =
        LiveDataReactiveStreams.fromPublisher(tvShowsUseCase.getAiringTodayTvShows(rxDisposer))

    fun topRatedTvShows(rxDisposer: RxDisposer): LiveData<UiState<TopRatedTvShows>> =
        LiveDataReactiveStreams.fromPublisher(tvShowsUseCase.getTopRatedTvShows(rxDisposer))

    fun popularTvShows(rxDisposer: RxDisposer): LiveData<UiState<PopularTvShows>> =
        LiveDataReactiveStreams.fromPublisher(tvShowsUseCase.getPopularTvShows(rxDisposer))

    fun onTheAirTvShows(rxDisposer: RxDisposer): LiveData<UiState<OnTheAirTvShows>> =
        LiveDataReactiveStreams.fromPublisher(tvShowsUseCase.getOnTheAirTvShows(rxDisposer))

    fun onTheAirTvShowsPaging(rxDisposer: RxDisposer): LiveData<UiState<PagingData<ListOnTheAirTvShows>>> =
        LiveDataReactiveStreams.fromPublisher(tvShowsUseCase.getOnTheAirTvShowsPaging(rxDisposer))

    fun popularTvShowsPaging(rxDisposer: RxDisposer): LiveData<UiState<PagingData<ListPopularTvShows>>> =
        LiveDataReactiveStreams.fromPublisher(tvShowsUseCase.getPopularTvShowsPaging(rxDisposer))

    fun topRatedTvShowsPaging(rxDisposer: RxDisposer): LiveData<UiState<PagingData<ListTopRatedTvShows>>> =
        LiveDataReactiveStreams.fromPublisher(tvShowsUseCase.getTopRatedTvShowsPaging(rxDisposer))
}