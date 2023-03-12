package com.application.zaki.movies.domain.usecase

import androidx.paging.PagingData
import com.application.zaki.movies.domain.interfaces.ITvShowsRepository
import com.application.zaki.movies.domain.interfaces.ITvShowsUseCase
import com.application.zaki.movies.domain.model.tvshows.*
import com.application.zaki.movies.utils.Genre
import io.reactivex.Flowable
import javax.inject.Inject

class TvShowsUseCase @Inject constructor(private val tvShowsRepository: ITvShowsRepository) :
    ITvShowsUseCase {
    override fun getAiringTodayTvShows(): Flowable<AiringTodayTvShows> =
        tvShowsRepository.getAiringTodayTvShows()

    override fun getDetailTvShows(tvId: String): Flowable<DetailTvShows> =
        tvShowsRepository.getDetailTvShows(tvId)

    override fun getOnTheAirTvShowsPaging(genre: Genre, totalPage: String): Flowable<PagingData<ListOnTheAirTvShows>> =
        tvShowsRepository.getOnTheAirTvShowsPaging(genre, totalPage)

    override fun getPopularTvShowsPaging(genre: Genre, totalPage: String): Flowable<PagingData<ListPopularTvShows>> =
        tvShowsRepository.getPopularTvShowsPaging(genre, totalPage)

    override fun getTopRatedTvShowsPaging(genre: Genre, totalPage: String): Flowable<PagingData<ListTopRatedTvShows>> =
        tvShowsRepository.getTopRatedTvShowsPaging(genre, totalPage)
}