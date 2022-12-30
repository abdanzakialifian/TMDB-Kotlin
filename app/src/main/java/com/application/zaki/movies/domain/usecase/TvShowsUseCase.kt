package com.application.zaki.movies.domain.usecase

import androidx.paging.PagingData
import com.application.zaki.movies.domain.interfaces.ITvShowsRepository
import com.application.zaki.movies.domain.interfaces.ITvShowsUseCase
import com.application.zaki.movies.domain.model.tvshows.*
import com.application.zaki.movies.utils.NetworkResult
import com.application.zaki.movies.utils.RxDisposer
import io.reactivex.Flowable
import javax.inject.Inject

class TvShowsUseCase @Inject constructor(private val tvShowsRepository: ITvShowsRepository) :
    ITvShowsUseCase {
    override fun getAiringTodayTvShows(rxDisposer: RxDisposer): Flowable<NetworkResult<AiringTodayTvShows>> =
        tvShowsRepository.getAiringTodayTvShows(rxDisposer)

    override fun getTopRatedTvShows(rxDisposer: RxDisposer): Flowable<NetworkResult<TopRatedTvShows>> =
        tvShowsRepository.getTopRatedTvShows(rxDisposer)

    override fun getPopularTvShows(rxDisposer: RxDisposer): Flowable<NetworkResult<PopularTvShows>> =
        tvShowsRepository.getPopularTvShows(rxDisposer)

    override fun getOnTheAirTvShows(rxDisposer: RxDisposer): Flowable<NetworkResult<OnTheAirTvShows>> =
        tvShowsRepository.getOnTheAirTvShows(rxDisposer)

    override fun getDetailTvShows(
        rxDisposer: RxDisposer,
        tvId: String,
    ): Flowable<NetworkResult<DetailTvShows>> = tvShowsRepository.getDetailTvShows(rxDisposer, tvId)

    override fun getOnTheAirTvShowsPaging(rxDisposer: RxDisposer): Flowable<NetworkResult<PagingData<ListOnTheAirTvShows>>> =
        tvShowsRepository.getOnTheAirTvShowsPaging(rxDisposer)

    override fun getPopularTvShowsPaging(rxDisposer: RxDisposer): Flowable<NetworkResult<PagingData<ListPopularTvShows>>> =
        tvShowsRepository.getPopularTvShowsPaging(rxDisposer)

    override fun getTopRatedTvShowsPaging(rxDisposer: RxDisposer): Flowable<NetworkResult<PagingData<ListTopRatedTvShows>>> =
        tvShowsRepository.getTopRatedTvShowsPaging(rxDisposer)
}