package com.application.zaki.movies.data.repository

import androidx.paging.PagingData
import androidx.paging.map
import com.application.zaki.movies.data.source.remote.RemoteDataSource
import com.application.zaki.movies.domain.interfaces.ITvShowsRepository
import com.application.zaki.movies.domain.model.tvshows.*
import com.application.zaki.movies.utils.DataMapperTvShows
import io.reactivex.Flowable
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TvShowsRepository @Inject constructor(private val remoteDataSource: RemoteDataSource) :
    ITvShowsRepository {
    override fun getAiringTodayTvShows(): Flowable<AiringTodayTvShows> =
        remoteDataSource.getAiringTodayTvShows()
            .map { data ->
                DataMapperTvShows.mapAiringTodayTvShowsResponseToAiringTodayTvShows(data)
            }

    override fun getTopRatedTvShows(): Flowable<TopRatedTvShows> =
        remoteDataSource.getTopRatedTvShows()
            .map { data ->
                DataMapperTvShows.mapTopRatedTvShowsResponseToTopRatedTvShows(data)
            }

    override fun getPopularTvShows(): Flowable<PopularTvShows> =
        remoteDataSource.getPopularTvShows()
            .map { data ->
                DataMapperTvShows.mapPopularTvShowsResponseToPopularTvShows(data)
            }

    override fun getOnTheAirTvShows(): Flowable<OnTheAirTvShows> =
        remoteDataSource.getOnTheAirTvShows()
            .map { data ->
                DataMapperTvShows.mapOnTheAirTvShowsResponseToOnTheAirTvShows(data)
            }

    override fun getDetailTvShows(tvId: String): Flowable<DetailTvShows> =
        remoteDataSource.getDetailTvShows(tvId)
            .map { data ->
                DataMapperTvShows.mapDetailTvShowsResponseToDetailTvShows(data)
            }

    override fun getOnTheAirTvShowsPaging(): Flowable<PagingData<ListOnTheAirTvShows>> =
        Flowable.zip(
            remoteDataSource.getOnTheAirTvShowsPaging(),
            remoteDataSource.getGenreTvShows()
        ) { onTheAirTvShowsPaging, genreTvShows ->
            return@zip onTheAirTvShowsPaging.map { data ->
                DataMapperTvShows.mapListOnTheAirTvShowsResponseToListOnTheAirTvShows(
                    data,
                    genreTvShows
                )
            }
        }

    override fun getPopularTvShowsPaging(): Flowable<PagingData<ListPopularTvShows>> = Flowable.zip(
        remoteDataSource.getPopularTvShowsPaging(),
        remoteDataSource.getGenreTvShows()
    ) { popularTvShowsPaging, genreTvShows ->
        return@zip popularTvShowsPaging.map {
            DataMapperTvShows.mapListPopularTvShowsResponseToListPopularTvShows(
                it,
                genreTvShows
            )
        }
    }

    override fun getTopRatedTvShowsPaging(): Flowable<PagingData<ListTopRatedTvShows>> =
        Flowable.zip(
            remoteDataSource.getTopRatedTvShowsPaging(),
            remoteDataSource.getGenreTvShows()
        ) { topRatedTvShowsPaging, genreTvShows ->
            return@zip topRatedTvShowsPaging.map {
                DataMapperTvShows.mapListTopRatedTvShowsResponseToListTopRatedTvShows(
                    it,
                    genreTvShows
                )
            }
        }
}