package com.application.zaki.movies.data.source.remote

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.rxjava2.flowable
import com.application.zaki.movies.data.source.remote.paging.movies.*
import com.application.zaki.movies.data.source.remote.paging.tvshows.OnTheAirTvShowsRxPagingSource
import com.application.zaki.movies.data.source.remote.paging.tvshows.PopularTvShowsRxPagingSource
import com.application.zaki.movies.data.source.remote.paging.tvshows.TopRatedTvShowsRxPagingSource
import com.application.zaki.movies.data.source.remote.response.movies.*
import com.application.zaki.movies.data.source.remote.response.tvshows.*
import com.application.zaki.movies.domain.model.tvshows.ListOnTheAirTvShows
import com.application.zaki.movies.domain.model.tvshows.ListTopRatedTvShows
import com.application.zaki.movies.utils.RxDisposer
import com.application.zaki.movies.utils.UiState
import io.reactivex.Flowable
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RemoteDataSource @Inject constructor(
    private val apiService: ApiService,
    private val reviewsMoviesRxPagingSource: ReviewsMoviesRxPagingSource,
    private val discoverMoviesRxPagingSource: DiscoverMoviesRxPagingSource,
    private val popularMoviesRxPagingSource: PopularMoviesRxPagingSource,
    private val topRatedMoviesRxPagingSource: TopRatedMoviesRxPagingSource,
    private val upComingMoviesRxPagingSource: UpComingMoviesRxPagingSource,
    private val onTheAirTvShowsRxPagingSource: OnTheAirTvShowsRxPagingSource,
    private val popularTvShowsRxPagingSource: PopularTvShowsRxPagingSource,
    private val topRatedTvShowsRxPagingSource: TopRatedTvShowsRxPagingSource,
) {
    fun getNowPlayingMovies(): Flowable<NowPlayingMoviesResponse> = apiService.getNowPlayingMovies()

    fun getTopRatedMovies(): Flowable<TopRatedMoviesResponse> = apiService.getTopRatedMovies()

    fun getPopularMovies(): Flowable<PopularMoviesResponse> = apiService.getPopularMovies()

    fun getUpComingMovies(): Flowable<UpComingMoviesResponse> = apiService.getUpComingMovies()

    fun getAiringTodayTvShows(): Flowable<AiringTodayTvShowsResponse> =
        apiService.getAiringTodayTvShows()

    fun getTopRatedTvShows(): Flowable<TopRatedTvShowsResponse> = apiService.getTopRatedTvShows()

    fun getPopularTvShows(): Flowable<PopularTvShowsResponse> = apiService.getPopularTvShows()

    fun getOnTheAirTvShows(): Flowable<OnTheAirTvShowsResponse> = apiService.getOnTheAirTvShows()

    fun getGenreMovies(): Flowable<GenreMoviesResponse> = apiService.getGenreMovies()

    fun getGenreTvShows(): Flowable<GenreTvShowsResponse> = apiService.getGenreTvShows()

    fun getDetailMovies(movieId: String): Flowable<DetailMoviesResponse> =
        apiService.getDetailMovies(movieId)

    fun getDetailTvShows(tvId: String): Flowable<DetailTvShowsResponse> =
        apiService.getDetailTvShows(tvId)

    fun getReviewsPaging(movieId: String): Flowable<PagingData<ReviewItemResponse>> = Pager(
        config = PagingConfig(
            pageSize = 10,
            enablePlaceholders = true,
            prefetchDistance = 2
        ),
        pagingSourceFactory = {
            reviewsMoviesRxPagingSource.apply {
                getMovieId(movieId)
            }
        }
    ).flowable

    fun getDiscoverMoviesPaging(genreId: String): Flowable<PagingData<ResultsItemDiscoverResponse>> =
        Pager(
            config = PagingConfig(
                pageSize = 10,
                enablePlaceholders = true,
                prefetchDistance = 2
            ),
            pagingSourceFactory = {
                discoverMoviesRxPagingSource.apply {
                    getGenreId(genreId)
                }
            }
        ).flowable

    fun getPopularMoviesPaging(): Flowable<PagingData<ListPopularMoviesResponse>> = Pager(
        config = PagingConfig(
            pageSize = 10,
            enablePlaceholders = true,
            prefetchDistance = 2
        ),
        pagingSourceFactory = {
            popularMoviesRxPagingSource
        }
    ).flowable

    fun getTopRatedMoviesPaging(): Flowable<PagingData<ListTopRatedMoviesResponse>> = Pager(
        config = PagingConfig(
            pageSize = 10,
            enablePlaceholders = true,
            prefetchDistance = 2
        ),
        pagingSourceFactory = {
            topRatedMoviesRxPagingSource
        }
    ).flowable

    fun getUpComingMoviesPaging(): Flowable<PagingData<ListUpComingMoviesResponse>> = Pager(
        config = PagingConfig(
            pageSize = 10,
            enablePlaceholders = true,
            prefetchDistance = 2
        ),
        pagingSourceFactory = {
            upComingMoviesRxPagingSource
        }
    ).flowable

    fun getOnTheAirTvShowsPaging(): Flowable<PagingData<ListOnTheAirTvShowsResponse>> = Pager(
        config = PagingConfig(
            pageSize = 10,
            enablePlaceholders = true,
            prefetchDistance = 2
        ),
        pagingSourceFactory = {
            onTheAirTvShowsRxPagingSource
        }
    ).flowable

    fun getPopularTvShowsPaging(): Flowable<PagingData<ListPopularTvShowsResponse>> =
        Pager(
            config = PagingConfig(
                pageSize = 10,
                enablePlaceholders = true,
                prefetchDistance = 2
            ),
            pagingSourceFactory = {
                popularTvShowsRxPagingSource
            }
        ).flowable

    fun getTopRatedTvShowsPaging(): Flowable<PagingData<ListTopRatedTvShowsResponse>> = Pager(
        config = PagingConfig(
            pageSize = 10,
            enablePlaceholders = true,
            prefetchDistance = 2
        ),
        pagingSourceFactory = {
            topRatedTvShowsRxPagingSource
        }
    ).flowable
}