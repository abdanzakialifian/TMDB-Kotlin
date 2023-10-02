package com.application.zaki.movies.data.source.remote

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.rxjava2.flowable
import com.application.zaki.movies.data.source.remote.paging.movies.MoviesRxPagingSource
import com.application.zaki.movies.data.source.remote.paging.other.DiscoverRxPagingSource
import com.application.zaki.movies.data.source.remote.paging.other.ReviewsRxPagingSource
import com.application.zaki.movies.data.source.remote.paging.tvshows.TvShowsRxPagingSource
import com.application.zaki.movies.data.source.remote.response.movies.DetailMoviesResponse
import com.application.zaki.movies.data.source.remote.response.movies.ListMoviesResponse
import com.application.zaki.movies.data.source.remote.response.other.ResultsItemDiscoverResponse
import com.application.zaki.movies.data.source.remote.response.other.ReviewItemResponse
import com.application.zaki.movies.data.source.remote.response.tvshows.DetailTvShowsResponse
import com.application.zaki.movies.data.source.remote.response.tvshows.ListTvShowsResponse
import com.application.zaki.movies.utils.Category
import com.application.zaki.movies.utils.Movie
import com.application.zaki.movies.utils.Page
import com.application.zaki.movies.utils.TvShow
import io.reactivex.Flowable
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RemoteDataSource @Inject constructor(
    private val apiService: ApiService,
    private val reviewsRxPagingSource: ReviewsRxPagingSource,
    private val discoverRxPagingSource: DiscoverRxPagingSource,
    private val moviesRxPagingSource: MoviesRxPagingSource,
    private val nowPlayingMoviesRxPagingSource: MoviesRxPagingSource,
    private val topRatedMoviesRxPagingSource: MoviesRxPagingSource,
    private val popularMoviesRxPagingSource: MoviesRxPagingSource,
    private val upComingMoviesRxPagingSource: MoviesRxPagingSource,
    private val tvShowsRxPagingSource: TvShowsRxPagingSource,
    private val airingTodayTvShowsRxPagingSource: TvShowsRxPagingSource,
    private val onTheAirTvShowsRxPagingSource: TvShowsRxPagingSource,
    private val popularTvShowsRxPagingSource: TvShowsRxPagingSource,
    private val topRatedTvShowsRxPagingSource: TvShowsRxPagingSource,
) {

    fun getDetailMovies(movieId: String): Flowable<DetailMoviesResponse> =
        apiService.getDetailMovies(movieId)

    fun getDetailTvShows(tvId: String): Flowable<DetailTvShowsResponse> =
        apiService.getDetailTvShows(tvId)

    fun getReviewsPaging(
        id: String, page: Page, category: Category
    ): Flowable<PagingData<ReviewItemResponse>> = Pager(config = PagingConfig(
        pageSize = 10, initialLoadSize = 10
    ), pagingSourceFactory = {
        reviewsRxPagingSource.apply {
            setDataReviews(id, page, category)
        }
    }).flowable

    fun getDiscoverPaging(
        genreId: String, category: Category
    ): Flowable<PagingData<ResultsItemDiscoverResponse>> = Pager(config = PagingConfig(
        pageSize = 10, initialLoadSize = 10
    ), pagingSourceFactory = {
        discoverRxPagingSource.apply {
            setDataDiscover(genreId, category)
        }
    }).flowable

    fun getMovies(
        movie: Movie?,
        page: Page?,
        query: String?,
        movieId: Int?
    ): Flowable<PagingData<ListMoviesResponse>> = Pager(
        config = PagingConfig(
            pageSize = 10,
            initialLoadSize = 10
        ), pagingSourceFactory = {
            when (movie) {
                Movie.NOW_PLAYING_MOVIES -> nowPlayingMoviesRxPagingSource.apply {
                    setData(movie, page, query, movieId)
                }

                Movie.POPULAR_MOVIES -> popularMoviesRxPagingSource.apply {
                    setData(movie, page, query, movieId)
                }

                Movie.TOP_RATED_MOVIES -> topRatedMoviesRxPagingSource.apply {
                    setData(movie, page, query, movieId)
                }

                Movie.UP_COMING_MOVIES -> upComingMoviesRxPagingSource.apply {
                    setData(movie, page, query, movieId)
                }

                else -> moviesRxPagingSource.apply {
                    setData(movie, page, query, movieId)
                }
            }
        }
    ).flowable

    fun getTvShows(
        tvShow: TvShow?,
        page: Page?,
        query: String?
    ): Flowable<PagingData<ListTvShowsResponse>> =
        Pager(
            config = PagingConfig(
                pageSize = 10, initialLoadSize = 10
            ), pagingSourceFactory = {
                when (tvShow) {
                    TvShow.AIRING_TODAY_TV_SHOWS -> airingTodayTvShowsRxPagingSource.apply {
                        setData(tvShow, page, query)
                    }

                    TvShow.TOP_RATED_TV_SHOWS -> topRatedTvShowsRxPagingSource.apply {
                        setData(tvShow, page, query)
                    }

                    TvShow.POPULAR_TV_SHOWS -> popularTvShowsRxPagingSource.apply {
                        setData(tvShow, page, query)
                    }

                    TvShow.ON_THE_AIR_TV_SHOWS -> onTheAirTvShowsRxPagingSource.apply {
                        setData(tvShow, page, query)
                    }

                    else -> tvShowsRxPagingSource.apply {
                        setData(tvShow, page, query)
                    }
                }
            }
        ).flowable
}