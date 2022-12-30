package com.application.zaki.movies.domain.interfaces

import androidx.paging.PagingData
import com.application.zaki.movies.domain.model.tvshows.*
import com.application.zaki.movies.utils.NetworkResult
import com.application.zaki.movies.utils.RxDisposer
import io.reactivex.Flowable

interface ITvShowsUseCase {
    fun getAiringTodayTvShows(rxDisposer: RxDisposer): Flowable<NetworkResult<AiringTodayTvShows>>
    fun getTopRatedTvShows(rxDisposer: RxDisposer): Flowable<NetworkResult<TopRatedTvShows>>
    fun getPopularTvShows(rxDisposer: RxDisposer): Flowable<NetworkResult<PopularTvShows>>
    fun getOnTheAirTvShows(rxDisposer: RxDisposer): Flowable<NetworkResult<OnTheAirTvShows>>
    fun getDetailTvShows(rxDisposer: RxDisposer, tvId: String): Flowable<NetworkResult<DetailTvShows>>
    fun getOnTheAirTvShowsPaging(rxDisposer: RxDisposer): Flowable<NetworkResult<PagingData<ListOnTheAirTvShows>>>
    fun getPopularTvShowsPaging(rxDisposer: RxDisposer): Flowable<NetworkResult<PagingData<ListPopularTvShows>>>
    fun getTopRatedTvShowsPaging(rxDisposer: RxDisposer): Flowable<NetworkResult<PagingData<ListTopRatedTvShows>>>
}