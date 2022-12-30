package com.application.zaki.movies.data.source.remote

import com.application.zaki.movies.data.source.remote.response.movies.*
import com.application.zaki.movies.data.source.remote.response.tvshows.*
import io.reactivex.Flowable
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("movie/now_playing")
    fun getNowPlayingMovies(): Flowable<NowPlayingMoviesResponse>

    @GET("movie/top_rated")
    fun getTopRatedMovies(): Flowable<TopRatedMoviesResponse>

    @GET("movie/top_rated")
    fun getTopRatedMoviesPaging(
        @Query("page") page: Int
    ): Single<TopRatedMoviesResponse>

    @GET("movie/popular")
    fun getPopularMovies(): Flowable<PopularMoviesResponse>

    @GET("movie/popular")
    fun getPopularMoviesPaging(
        @Query("page") page: Int
    ): Single<PopularMoviesResponse>

    @GET("movie/upcoming")
    fun getUpComingMovies(): Flowable<UpComingMoviesResponse>

    @GET("movie/upcoming")
    fun getUpComingMoviesPaging(
        @Query("page") page: Int
    ): Single<UpComingMoviesResponse>

    @GET("tv/airing_today")
    fun getAiringTodayTvShows(): Flowable<AiringTodayTvShowsResponse>

    @GET("tv/top_rated")
    fun getTopRatedTvShows(): Flowable<TopRatedTvShowsResponse>

    @GET("tv/top_rated")
    fun getTopRatedTvShowsPaging(
        @Query("page") page: Int
    ): Single<TopRatedTvShowsResponse>

    @GET("tv/popular")
    fun getPopularTvShows(): Flowable<PopularTvShowsResponse>

    @GET("tv/popular")
    fun getPopularTvShowsPaging(
        @Query("page") page: Int
    ): Single<PopularTvShowsResponse>

    @GET("tv/on_the_air")
    fun getOnTheAirTvShows(): Flowable<OnTheAirTvShowsResponse>

    @GET("tv/on_the_air")
    fun getOnTheAirTvShowsPaging(
        @Query("page") page: Int
    ): Single<OnTheAirTvShowsResponse>

    @GET("genre/movie/list")
    fun getGenreMovies(): Flowable<GenreMoviesResponse>

    @GET("genre/tv/list")
    fun getGenreTvShows(): Flowable<GenreTvShowsResponse>

    @GET("movie/{movie_id}?append_to_response=credits,videos")
    fun getDetailMovies(
        @Path("movie_id") movieId: String
    ): Flowable<DetailMoviesResponse>

    @GET("tv/{tv_id}?append_to_response=credits,videos")
    fun getDetailTvShows(
        @Path("tv_id") tvId: String
    ): Flowable<DetailTvShowsResponse>

    @GET("movie/{movie_id}/reviews")
    fun getReviewsMovie(
        @Path("movie_id") movieId: String
    ): Flowable<ReviewsMovieResponse>

    @GET("discover/movie")
    fun getDiscoverMovie(
        @Query("page") page: Int,
        @Query("with_genres") withGenres: String
    ): Single<DiscoverMovieResponse>
}