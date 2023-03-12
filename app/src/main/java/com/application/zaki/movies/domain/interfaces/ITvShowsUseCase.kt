package com.application.zaki.movies.domain.interfaces

import androidx.paging.PagingData
import com.application.zaki.movies.domain.model.tvshows.*
import com.application.zaki.movies.utils.Genre
import io.reactivex.Flowable

interface ITvShowsUseCase {
    fun getAiringTodayTvShows(): Flowable<AiringTodayTvShows>

    fun getDetailTvShows(tvId: String): Flowable<DetailTvShows>

    fun getOnTheAirTvShowsPaging(genre: Genre, totalPage: String): Flowable<PagingData<ListOnTheAirTvShows>>

    fun getPopularTvShowsPaging(genre: Genre, totalPage: String): Flowable<PagingData<ListPopularTvShows>>

    fun getTopRatedTvShowsPaging(genre: Genre, totalPage: String): Flowable<PagingData<ListTopRatedTvShows>>
}