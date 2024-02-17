package com.application.tmdb.core.source.remote

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.rxjava2.flowable
import com.application.tmdb.common.response.movies.DetailMoviesResponse
import com.application.tmdb.common.response.movies.ListMoviesResponse
import com.application.tmdb.common.response.other.DetailCastResponse
import com.application.tmdb.common.response.other.ResultsItemDiscoverResponse
import com.application.tmdb.common.response.other.ReviewItemResponse
import com.application.tmdb.common.response.tvshows.DetailTvShowsResponse
import com.application.tmdb.common.response.tvshows.ListTvShowsResponse
import com.application.tmdb.common.utils.Category
import com.application.tmdb.common.utils.Movie
import com.application.tmdb.common.utils.Page
import com.application.tmdb.common.utils.TvShow
import com.application.tmdb.core.source.remote.paging.movies.MoviesRxPagingSource
import com.application.tmdb.core.source.remote.paging.other.DiscoverRxPagingSource
import com.application.tmdb.core.source.remote.paging.other.ReviewsRxPagingSource
import com.application.tmdb.core.source.remote.paging.tvshows.TvShowsRxPagingSource
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
        id: String?, category: Category?
    ): Flowable<PagingData<ReviewItemResponse>> = Pager(config = PagingConfig(
        pageSize = 10, initialLoadSize = 10
    ), pagingSourceFactory = {
        reviewsRxPagingSource.apply {
            setDataReviews(id, category)
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
        query: String?,
        tvId: Int?
    ): Flowable<PagingData<ListTvShowsResponse>> =
        Pager(
            config = PagingConfig(
                pageSize = 10, initialLoadSize = 10
            ), pagingSourceFactory = {
                when (tvShow) {
                    TvShow.AIRING_TODAY_TV_SHOWS -> airingTodayTvShowsRxPagingSource.apply {
                        setData(tvShow, page, query, tvId)
                    }

                    TvShow.TOP_RATED_TV_SHOWS -> topRatedTvShowsRxPagingSource.apply {
                        setData(tvShow, page, query, tvId)
                    }

                    TvShow.POPULAR_TV_SHOWS -> popularTvShowsRxPagingSource.apply {
                        setData(tvShow, page, query, tvId)
                    }

                    TvShow.ON_THE_AIR_TV_SHOWS -> onTheAirTvShowsRxPagingSource.apply {
                        setData(tvShow, page, query, tvId)
                    }

                    else -> tvShowsRxPagingSource.apply {
                        setData(tvShow, page, query, tvId)
                    }
                }
            }
        ).flowable

    fun getDetailCast(personId: Int): Flowable<DetailCastResponse> =
        apiService.getDetailCast(personId)
}