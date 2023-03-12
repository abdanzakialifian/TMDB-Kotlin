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
import com.application.zaki.movies.data.source.remote.response.movies.DetailMoviesResponse
import com.application.zaki.movies.data.source.remote.response.movies.ListMoviesResponse
import com.application.zaki.movies.data.source.remote.response.tvshows.*
import com.application.zaki.movies.utils.Genre
import com.application.zaki.movies.utils.Movie
import com.application.zaki.movies.utils.Page
import io.reactivex.Flowable
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RemoteDataSource @Inject constructor(
    private val apiService: ApiService,
    private val reviewsRxPagingSource: ReviewsRxPagingSource,
    private val discoverRxPagingSource: DiscoverRxPagingSource,
    private val nowPlayingMoviesRxPagingSource: MoviesRxPagingSource,
    private val topRatedMoviesRxPagingSource: MoviesRxPagingSource,
    private val popularMoviesRxPagingSource: MoviesRxPagingSource,
    private val upComingMoviesRxPagingSource: MoviesRxPagingSource,
    private val onTheAirTvShowsRxPagingSource: OnTheAirTvShowsRxPagingSource,
    private val popularTvShowsRxPagingSource: PopularTvShowsRxPagingSource,
    private val topRatedTvShowsRxPagingSource: TopRatedTvShowsRxPagingSource,
) {
    fun getAiringTodayTvShows(): Flowable<AiringTodayTvShowsResponse> =
        apiService.getAiringTodayTvShows()

    fun getGenre(genre: Genre): Flowable<GenreResponse> =
        if (genre == Genre.MOVIES) apiService.getGenreMovies() else apiService.getGenreTvShows()

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

    fun getMovies(
        movie: Movie,
        page: Page
    ): Flowable<PagingData<ListMoviesResponse>> = Pager(
        config = PagingConfig(
            pageSize = 10,
            initialLoadSize = 10,
            enablePlaceholders = true
        ),
        pagingSourceFactory = {
            when(movie) {
                Movie.NOW_PLAYING_MOVIES -> nowPlayingMoviesRxPagingSource.apply {
                    setData(movie, page)
                }
                Movie.POPULAR_MOVIES -> popularMoviesRxPagingSource.apply {
                    setData(movie, page)
                }
                Movie.TOP_RATED_MOVIES -> topRatedMoviesRxPagingSource.apply {
                    setData(movie, page)
                }
                Movie.UP_COMING_MOVIES -> upComingMoviesRxPagingSource.apply {
                    setData(movie, page)
                }
            }
        }
    ).flowable

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
}