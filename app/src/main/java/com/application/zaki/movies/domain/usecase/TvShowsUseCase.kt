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
    override fun getAiringTodayTvShows(): Flowable<AiringTodayTvShows> =
        tvShowsRepository.getAiringTodayTvShows()

    override fun getTopRatedTvShows(): Flowable<TopRatedTvShows> =
        tvShowsRepository.getTopRatedTvShows()

    override fun getPopularTvShows(): Flowable<PopularTvShows> =
        tvShowsRepository.getPopularTvShows()

    override fun getOnTheAirTvShows(): Flowable<OnTheAirTvShows> =
        tvShowsRepository.getOnTheAirTvShows()

    override fun getDetailTvShows(tvId: String): Flowable<DetailTvShows> =
        tvShowsRepository.getDetailTvShows(tvId)

    override fun getOnTheAirTvShowsPaging(): Flowable<PagingData<ListOnTheAirTvShows>> =
        tvShowsRepository.getOnTheAirTvShowsPaging()

    override fun getPopularTvShowsPaging(): Flowable<PagingData<ListPopularTvShows>> =
        tvShowsRepository.getPopularTvShowsPaging()

    override fun getTopRatedTvShowsPaging(): Flowable<PagingData<ListTopRatedTvShows>> =
        tvShowsRepository.getTopRatedTvShowsPaging()
}