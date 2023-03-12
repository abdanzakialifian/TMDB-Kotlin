package com.application.zaki.movies.data.repository

import androidx.paging.PagingData
import androidx.paging.map
import com.application.zaki.movies.data.source.remote.RemoteDataSource
import com.application.zaki.movies.domain.interfaces.ITvShowsRepository
import com.application.zaki.movies.domain.model.tvshows.*
import com.application.zaki.movies.utils.DataMapperTvShows
import com.application.zaki.movies.utils.Genre
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

    override fun getDetailTvShows(tvId: String): Flowable<DetailTvShows> =
        remoteDataSource.getDetailTvShows(tvId)
            .map { data ->
                DataMapperTvShows.mapDetailTvShowsResponseToDetailTvShows(data)
            }

    override fun getOnTheAirTvShowsPaging(
        genre: Genre,
        totalPage: String
    ): Flowable<PagingData<ListOnTheAirTvShows>> =
        Flowable.zip(
            remoteDataSource.getOnTheAirTvShowsPaging(totalPage),
            remoteDataSource.getGenre(genre)
        ) { onTheAirTvShowsPaging, genreTvShows ->
            return@zip onTheAirTvShowsPaging.map { data ->
                DataMapperTvShows.mapListOnTheAirTvShowsResponseToListOnTheAirTvShows(
                    data,
                    genreTvShows
                )
            }
        }

    override fun getPopularTvShowsPaging(
        genre: Genre,
        totalPage: String
    ): Flowable<PagingData<ListPopularTvShows>> =
        Flowable.zip(
            remoteDataSource.getPopularTvShowsPaging(totalPage),
            remoteDataSource.getGenre(genre)
        ) { popularTvShowsPaging, genreTvShows ->
            return@zip popularTvShowsPaging.map {
                DataMapperTvShows.mapListPopularTvShowsResponseToListPopularTvShows(
                    it,
                    genreTvShows
                )
            }
        }

    override fun getTopRatedTvShowsPaging(
        genre: Genre,
        totalPage: String
    ): Flowable<PagingData<ListTopRatedTvShows>> =
        Flowable.zip(
            remoteDataSource.getTopRatedTvShowsPaging(totalPage),
            remoteDataSource.getGenre(genre)
        ) { topRatedTvShowsPaging, genreTvShows ->
            return@zip topRatedTvShowsPaging.map {
                DataMapperTvShows.mapListTopRatedTvShowsResponseToListTopRatedTvShows(
                    it,
                    genreTvShows
                )
            }
        }
}