package com.application.zaki.movies.data.source.remote

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.rxjava2.flowable
import com.application.zaki.movies.data.source.remote.paging.combine.DiscoverRxPagingSource
import com.application.zaki.movies.data.source.remote.paging.combine.ReviewsRxPagingSource
import com.application.zaki.movies.data.source.remote.paging.movies.*
import com.application.zaki.movies.data.source.remote.paging.tvshows.OnTheAirTvShowsRxPagingSource
import com.application.zaki.movies.data.source.remote.paging.tvshows.PopularTvShowsRxPagingSource
import com.application.zaki.movies.data.source.remote.paging.tvshows.TopRatedTvShowsRxPagingSource
import com.application.zaki.movies.data.source.remote.response.combine.GenreResponse
import com.application.zaki.movies.data.source.remote.response.combine.ResultsItemDiscoverResponse
import com.application.zaki.movies.data.source.remote.response.combine.ReviewItemResponse
import com.application.zaki.movies.data.source.remote.response.movies.*
import com.application.zaki.movies.data.source.remote.response.tvshows.*
import io.reactivex.Flowable
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RemoteDataSource @Inject constructor(
    private val apiService: ApiService,
    private val reviewsRxPagingSource: ReviewsRxPagingSource,
    private val discoverRxPagingSource: DiscoverRxPagingSource,
    private val popularMoviesRxPagingSource: PopularMoviesRxPagingSource,
    private val topRatedMoviesRxPagingSource: TopRatedMoviesRxPagingSource,
    private val upComingMoviesRxPagingSource: UpComingMoviesRxPagingSource,
    private val onTheAirTvShowsRxPagingSource: OnTheAirTvShowsRxPagingSource,
    private val popularTvShowsRxPagingSource: PopularTvShowsRxPagingSource,
    private val topRatedTvShowsRxPagingSource: TopRatedTvShowsRxPagingSource,
) {
    fun getNowPlayingMovies(): Flowable<NowPlayingMoviesResponse> = apiService.getNowPlayingMovies()

    fun getAiringTodayTvShows(): Flowable<AiringTodayTvShowsResponse> =
        apiService.getAiringTodayTvShows()

    fun getGenre(type: String): Flowable<GenreResponse> =
        if (type == MOVIES) apiService.getGenreMovies() else apiService.getGenreTvShows()

    fun getDetailMovies(movieId: String): Flowable<DetailMoviesResponse> =
        apiService.getDetailMovies(movieId)

    fun getDetailTvShows(tvId: String): Flowable<DetailTvShowsResponse> =
        apiService.getDetailTvShows(tvId)

    fun getReviewsPaging(
        id: String, totalPage: String, type: String
    ): Flowable<PagingData<ReviewItemResponse>> = Pager(config = PagingConfig(
        pageSize = 10, enablePlaceholders = true, prefetchDistance = 2
    ), pagingSourceFactory = {
        reviewsRxPagingSource.apply {
            setDataReviews(id, totalPage, type)
        }
    }).flowable

    fun getDiscoverPaging(
        genreId: String,
        type: String
    ): Flowable<PagingData<ResultsItemDiscoverResponse>> =
        Pager(config = PagingConfig(
            pageSize = 10, enablePlaceholders = true, prefetchDistance = 2
        ), pagingSourceFactory = {
            discoverRxPagingSource.apply {
                setDataDiscover(genreId, type)
            }
        }).flowable

    fun getPopularMoviesPaging(totalPage: String): Flowable<PagingData<ListPopularMoviesResponse>> =
        Pager(config = PagingConfig(
            pageSize = 10, enablePlaceholders = true, prefetchDistance = 2
        ), pagingSourceFactory = {
            popularMoviesRxPagingSource.apply {
                setTotalPage(totalPage)
            }
        }).flowable

    fun getTopRatedMoviesPaging(totalPage: String): Flowable<PagingData<ListTopRatedMoviesResponse>> =
        Pager(config = PagingConfig(
            pageSize = 10, enablePlaceholders = true, prefetchDistance = 2
        ), pagingSourceFactory = {
            topRatedMoviesRxPagingSource.apply {
                setTotalPage(totalPage)
            }
        }).flowable

    fun getUpComingMoviesPaging(totalPage: String): Flowable<PagingData<ListUpComingMoviesResponse>> =
        Pager(config = PagingConfig(
            pageSize = 10, enablePlaceholders = true, prefetchDistance = 2
        ), pagingSourceFactory = {
            upComingMoviesRxPagingSource.apply {
                setTotalPage(totalPage)
            }
        }).flowable

    fun getOnTheAirTvShowsPaging(totalPage: String): Flowable<PagingData<ListOnTheAirTvShowsResponse>> =
        Pager(config = PagingConfig(
            pageSize = 10, enablePlaceholders = true, prefetchDistance = 2
        ), pagingSourceFactory = {
            onTheAirTvShowsRxPagingSource.apply {
                setTotalPage(totalPage)
            }
        }).flowable

    fun getPopularTvShowsPaging(totalPage: String): Flowable<PagingData<ListPopularTvShowsResponse>> =
        Pager(config = PagingConfig(
            pageSize = 10, enablePlaceholders = true, prefetchDistance = 2
        ), pagingSourceFactory = {
            popularTvShowsRxPagingSource.apply {
                setTotalPage(totalPage)
            }
        }).flowable

    fun getTopRatedTvShowsPaging(totalPage: String): Flowable<PagingData<ListTopRatedTvShowsResponse>> =
        Pager(config = PagingConfig(
            pageSize = 10, enablePlaceholders = true, prefetchDistance = 2
        ), pagingSourceFactory = {
            topRatedTvShowsRxPagingSource.apply {
                setTotalPage(totalPage)
            }
        }).flowable

    companion object {
        const val MOVIES = "MOVIES"
        const val TV_SHOWS = "TV SHOWS"
    }
}