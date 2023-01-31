package com.application.zaki.movies.domain.usecase

import androidx.paging.PagingData
import com.application.zaki.movies.domain.interfaces.ITvShowsRepository
import com.application.zaki.movies.domain.interfaces.ITvShowsUseCase
import com.application.zaki.movies.domain.model.tvshows.*
import com.application.zaki.movies.utils.UiState
import com.application.zaki.movies.utils.RxDisposer
import io.reactivex.Flowable
import javax.inject.Inject

class TvShowsUseCase @Inject constructor(private val tvShowsRepository: ITvShowsRepository) :
    ITvShowsUseCase {
    override fun getAiringTodayTvShows(rxDisposer: RxDisposer): Flowable<UiState<AiringTodayTvShows>> =
        tvShowsRepository.getAiringTodayTvShows(rxDisposer)

    override fun getTopRatedTvShows(rxDisposer: RxDisposer): Flowable<UiState<TopRatedTvShows>> =
        tvShowsRepository.getTopRatedTvShows(rxDisposer)

    override fun getPopularTvShows(rxDisposer: RxDisposer): Flowable<UiState<PopularTvShows>> =
        tvShowsRepository.getPopularTvShows(rxDisposer)

    override fun getOnTheAirTvShows(rxDisposer: RxDisposer): Flowable<UiState<OnTheAirTvShows>> =
        tvShowsRepository.getOnTheAirTvShows(rxDisposer)

    override fun getDetailTvShows(
        rxDisposer: RxDisposer,
        tvId: String,
    ): Flowable<UiState<DetailTvShows>> = tvShowsRepository.getDetailTvShows(rxDisposer, tvId)

    override fun getOnTheAirTvShowsPaging(rxDisposer: RxDisposer): Flowable<UiState<PagingData<ListOnTheAirTvShows>>> =
        tvShowsRepository.getOnTheAirTvShowsPaging(rxDisposer)

    override fun getPopularTvShowsPaging(rxDisposer: RxDisposer): Flowable<UiState<PagingData<ListPopularTvShows>>> =
        tvShowsRepository.getPopularTvShowsPaging(rxDisposer)

    override fun getTopRatedTvShowsPaging(rxDisposer: RxDisposer): Flowable<UiState<PagingData<ListTopRatedTvShows>>> =
        tvShowsRepository.getTopRatedTvShowsPaging(rxDisposer)
}