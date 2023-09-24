package com.application.zaki.movies.domain.usecase

import androidx.paging.PagingData
import androidx.paging.rxjava2.cachedIn
import com.application.zaki.movies.domain.interfaces.ITvShowsRepository
import com.application.zaki.movies.domain.model.MovieTvShow
import com.application.zaki.movies.utils.Page
import com.application.zaki.movies.utils.TvShow
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
        airingTodayTvShow: TvShow,
        topRatedTvShow: TvShow,
        popularTvShow: TvShow,
        onTheAirTvShow: TvShow,
        page: Page,
        scope: CoroutineScope
    ): Flowable<List<Pair<TvShow, PagingData<MovieTvShow>>>> {
        return Flowable.zip(
            iTvShowsRepository.getTvShows(airingTodayTvShow, page).cachedIn(scope),
            iTvShowsRepository.getTvShows(topRatedTvShow, page).cachedIn(scope),
            iTvShowsRepository.getTvShows(popularTvShow, page).cachedIn(scope),
            iTvShowsRepository.getTvShows(onTheAirTvShow, page).cachedIn(scope),
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