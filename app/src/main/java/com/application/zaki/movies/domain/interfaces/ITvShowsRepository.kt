package com.application.zaki.movies.domain.interfaces

import androidx.paging.PagingData
import com.application.zaki.movies.domain.model.tvshows.*
import com.application.zaki.movies.utils.UiState
import com.application.zaki.movies.utils.RxDisposer
import io.reactivex.Flowable

interface ITvShowsRepository {
    fun getAiringTodayTvShows(rxDisposer: RxDisposer): Flowable<UiState<AiringTodayTvShows>>
    fun getTopRatedTvShows(rxDisposer: RxDisposer): Flowable<UiState<TopRatedTvShows>>
    fun getPopularTvShows(rxDisposer: RxDisposer): Flowable<UiState<PopularTvShows>>
    fun getOnTheAirTvShows(rxDisposer: RxDisposer): Flowable<UiState<OnTheAirTvShows>>
    fun getDetailTvShows(
        rxDisposer: RxDisposer,
        tvId: String
    ): Flowable<UiState<DetailTvShows>>

    fun getOnTheAirTvShowsPaging(rxDisposer: RxDisposer): Flowable<UiState<PagingData<ListOnTheAirTvShows>>>
    fun getPopularTvShowsPaging(rxDisposer: RxDisposer): Flowable<UiState<PagingData<ListPopularTvShows>>>
    fun getTopRatedTvShowsPaging(rxDisposer: RxDisposer): Flowable<UiState<PagingData<ListTopRatedTvShows>>>
}