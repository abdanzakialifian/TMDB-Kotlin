package com.application.tmdb.core.domain.usecase

import androidx.paging.PagingData
import androidx.paging.rxjava2.cachedIn
import com.application.tmdb.core.domain.interfaces.ITvShowsRepository
import com.application.tmdb.core.domain.model.MovieTvShowModel
import com.application.tmdb.common.Page
import com.application.tmdb.common.TvShow
import io.reactivex.Flowable
import io.reactivex.functions.Function4
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject
import javax.inject.Singleton

@OptIn(ExperimentalCoroutinesApi::class)
@Singleton
class GetListAllTvShows @Inject constructor(private val iTvShowsRepository: ITvShowsRepository) {
    operator fun invoke(
        airingTodayTvShow: com.application.tmdb.common.TvShow?,
        topRatedTvShow: com.application.tmdb.common.TvShow?,
        popularTvShow: com.application.tmdb.common.TvShow?,
        onTheAirTvShow: com.application.tmdb.common.TvShow?,
        page: com.application.tmdb.common.Page?,
        query: String?,
        tvId: Int?,
        scope: CoroutineScope
    ): Flowable<List<Pair<com.application.tmdb.common.TvShow, PagingData<MovieTvShowModel>>>> {
        return Flowable.zip(
            iTvShowsRepository.getTvShows(airingTodayTvShow, page, query, tvId).cachedIn(scope),
            iTvShowsRepository.getTvShows(topRatedTvShow, page, query, tvId).cachedIn(scope),
            iTvShowsRepository.getTvShows(popularTvShow, page, query, tvId).cachedIn(scope),
            iTvShowsRepository.getTvShows(onTheAirTvShow, page, query, tvId).cachedIn(scope),
            Function4 { airingTodayTvShowPaging, topRatedTvShowPaging, popularTvShowPaging, onTheAirTvShowPaging ->
                return@Function4 listOf(
                    Pair(com.application.tmdb.common.TvShow.AIRING_TODAY_TV_SHOWS, airingTodayTvShowPaging),
                    Pair(com.application.tmdb.common.TvShow.TOP_RATED_TV_SHOWS, topRatedTvShowPaging),
                    Pair(com.application.tmdb.common.TvShow.POPULAR_TV_SHOWS, popularTvShowPaging),
                    Pair(com.application.tmdb.common.TvShow.ON_THE_AIR_TV_SHOWS, onTheAirTvShowPaging)
                )
            }
        )
    }
}