package com.application.zaki.movies.domain.interfaces

import androidx.paging.PagingData
import com.application.zaki.movies.domain.model.tvshows.*
import io.reactivex.Flowable

interface ITvShowsUseCase {
    fun getAiringTodayTvShows(): Flowable<AiringTodayTvShows>

    fun getDetailTvShows(tvId: String): Flowable<DetailTvShows>

    fun getOnTheAirTvShowsPaging(type: String, totalPage: String): Flowable<PagingData<ListOnTheAirTvShows>>

    fun getPopularTvShowsPaging(type: String, totalPage: String): Flowable<PagingData<ListPopularTvShows>>

    fun getTopRatedTvShowsPaging(type: String, totalPage: String): Flowable<PagingData<ListTopRatedTvShows>>
}