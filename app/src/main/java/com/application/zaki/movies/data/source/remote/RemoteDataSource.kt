package com.application.zaki.movies.data.source.remote

import com.application.zaki.movies.data.source.remote.response.movies.*
import com.application.zaki.movies.data.source.remote.response.tvshows.*
import io.reactivex.Flowable
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RemoteDataSource @Inject constructor(private val apiService: ApiService) {

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

    fun getReviews(movieId: String): Flowable<ReviewsMovieResponse> =
        apiService.getReviewsMovie(movieId)
}