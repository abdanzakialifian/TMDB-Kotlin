package com.application.zaki.movies.domain.usecase

import androidx.paging.PagingData
import androidx.paging.map
import com.application.zaki.movies.domain.interfaces.ITvShowsRepository
import com.application.zaki.movies.domain.model.MovieTvShow
import com.application.zaki.movies.utils.Category
import com.application.zaki.movies.utils.DataMapper.toMovieTvShowWithGenres
import com.application.zaki.movies.utils.Page
import com.application.zaki.movies.utils.TvShow
import io.reactivex.Flowable
import io.reactivex.functions.Function4
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetListAllTvShows @Inject constructor(private val iTvShowsRepository: ITvShowsRepository) {
    operator fun invoke(
        airingTodayTvShow: TvShow,
        topRatedTvShow: TvShow,
        popularTvShow: TvShow,
        onTheAirTvShow: TvShow,
        category: Category,
        page: Page
    ): Flowable<List<Pair<TvShow, PagingData<MovieTvShow>>>> {
        val airingTodayTvShowFlowable = Flowable.zip(
            iTvShowsRepository.getTvShows(airingTodayTvShow, page),
            iTvShowsRepository.getGenres(category)
        ) { tvShows, genres ->
            return@zip tvShows.map { map ->
                map.toMovieTvShowWithGenres(genres)
            }
        }

        val topRatedTvShowFlowable = Flowable.zip(
            iTvShowsRepository.getTvShows(topRatedTvShow, page),
            iTvShowsRepository.getGenres(category)
        ) { tvShows, genres ->
            return@zip tvShows.map { map ->
                map.toMovieTvShowWithGenres(genres)
            }
        }

        val popularTvShowFlowable = Flowable.zip(
            iTvShowsRepository.getTvShows(popularTvShow, page),
            iTvShowsRepository.getGenres(category)
        ) { tvShows, genres ->
            return@zip tvShows.map { map ->
                map.toMovieTvShowWithGenres(genres)
            }
        }

        val onTheAirTvShowFlowable = Flowable.zip(
            iTvShowsRepository.getTvShows(onTheAirTvShow, page),
            iTvShowsRepository.getGenres(category)
        ) { tvShows, genres ->
            return@zip tvShows.map { map ->
                map.toMovieTvShowWithGenres(genres)
            }
        }

        return Flowable.zip(
            airingTodayTvShowFlowable,
            topRatedTvShowFlowable,
            popularTvShowFlowable,
            onTheAirTvShowFlowable,
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