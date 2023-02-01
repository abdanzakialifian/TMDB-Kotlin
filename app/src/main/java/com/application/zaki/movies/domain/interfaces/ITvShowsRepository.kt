package com.application.zaki.movies.domain.interfaces

import androidx.paging.PagingData
import com.application.zaki.movies.domain.model.tvshows.*
import io.reactivex.Flowable

interface ITvShowsRepository {
    fun getAiringTodayTvShows(): Flowable<AiringTodayTvShows>

    fun getTopRatedTvShows(): Flowable<TopRatedTvShows>

    fun getPopularTvShows(): Flowable<PopularTvShows>

    fun getOnTheAirTvShows(): Flowable<OnTheAirTvShows>

    fun getDetailTvShows(tvId: String): Flowable<DetailTvShows>

    fun getOnTheAirTvShowsPaging(): Flowable<PagingData<ListOnTheAirTvShows>>

    fun getPopularTvShowsPaging(): Flowable<PagingData<ListPopularTvShows>>

    fun getTopRatedTvShowsPaging(): Flowable<PagingData<ListTopRatedTvShows>>
}