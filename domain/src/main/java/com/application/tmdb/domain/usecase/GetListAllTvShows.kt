package com.application.tmdb.domain.usecase

import androidx.paging.PagingData
import androidx.paging.rxjava2.cachedIn
import com.application.tmdb.core.utils.Page
import com.application.tmdb.core.utils.TvShow
import com.application.tmdb.domain.interfaces.ITvShowsRepository
import com.application.tmdb.domain.model.MovieTvShowModel
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
        airingTodayTvShow: TvShow?,
        topRatedTvShow: TvShow?,
        popularTvShow: TvShow?,
        onTheAirTvShow: TvShow?,
        page: Page?,
        query: String?,
        tvId: Int?,
        scope: CoroutineScope
    ): Flowable<List<Pair<TvShow, PagingData<MovieTvShowModel>>>> {
        return Flowable.zip(
            iTvShowsRepository.getTvShows(airingTodayTvShow, page, query, tvId).cachedIn(scope),
            iTvShowsRepository.getTvShows(topRatedTvShow, page, query, tvId).cachedIn(scope),
            iTvShowsRepository.getTvShows(popularTvShow, page, query, tvId).cachedIn(scope),
            iTvShowsRepository.getTvShows(onTheAirTvShow, page, query, tvId).cachedIn(scope),
            Function4 { airingTodayTvShowPaging, topRatedTvShowPaging, popularTvShowPaging, onTheAirTvShowPaging ->
                return@Function4 listOf(
                    Pair(TvShow.AIRING_TODAY_TV_SHOWS, airingTodayTvShowPaging),
                    Pair(TvShow.TOP_RATED_TV_SHOWS, topRatedTvShowPaging),
                    Pair(TvShow.POPULAR_TV_SHOWS, popularTvShowPaging),
                    Pair(TvShow.ON_THE_AIR_TV_SHOWS, onTheAirTvShowPaging)
                )
            }
        )
    }
}