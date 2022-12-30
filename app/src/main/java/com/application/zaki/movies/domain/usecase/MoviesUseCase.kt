package com.application.zaki.movies.domain.usecase

import androidx.paging.PagingData
import com.application.zaki.movies.domain.interfaces.IMoviesRepository
import com.application.zaki.movies.domain.interfaces.IMoviesUseCase
import com.application.zaki.movies.domain.model.movies.*
import com.application.zaki.movies.utils.NetworkResult
import com.application.zaki.movies.utils.RxDisposer
import io.reactivex.Flowable
import javax.inject.Inject

class MoviesUseCase @Inject constructor(private val moviesRepository: IMoviesRepository) :
    IMoviesUseCase {
    override fun getPopularMovies(rxDisposer: RxDisposer): Flowable<NetworkResult<PopularMovies>> =
        moviesRepository.getPopularMovies(rxDisposer)

    override fun getNowPlayingMovies(rxDisposer: RxDisposer): Flowable<NetworkResult<NowPlayingMovies>> =
        moviesRepository.getNowPlayingMovies(rxDisposer)

    override fun getTopRatedMovies(rxDisposer: RxDisposer): Flowable<NetworkResult<TopRatedMovies>> =
        moviesRepository.getTopRatedMovies(rxDisposer)

    override fun getUpComingMovies(rxDisposer: RxDisposer): Flowable<NetworkResult<UpComingMovies>> =
        moviesRepository.getUpComingMovies(rxDisposer)

    override fun getDetailMovies(
        rxDisposer: RxDisposer,
        movieId: String,
    ): Flowable<NetworkResult<DetailMovies>> = moviesRepository.getDetailMovies(rxDisposer, movieId)

    override fun getPopularMoviesPaging(rxDisposer: RxDisposer): Flowable<NetworkResult<PagingData<ListPopularMovies>>> =
        moviesRepository.getPopularMoviesPaging(rxDisposer)

    override fun getTopRatedMoviesPaging(rxDisposer: RxDisposer): Flowable<NetworkResult<PagingData<ListTopRatedMovies>>> =
        moviesRepository.getTopRatedMoviesPaging(rxDisposer)

    override fun getUpComingMoviesPaging(rxDisposer: RxDisposer): Flowable<NetworkResult<PagingData<ListUpComingMovies>>> =
        moviesRepository.getUpComingMoviesPaging(rxDisposer)

    override fun getReviewsMoviePaging(
        rxDisposer: RxDisposer,
        movieId: String
    ): Flowable<NetworkResult<ReviewsMovie>> =
        moviesRepository.getReviewsMoviePaging(rxDisposer, movieId)

    override fun getDiscoverMovies(
        rxDisposer: RxDisposer,
        genreId: String
    ): Flowable<NetworkResult<PagingData<ResultsItemDiscover>>> =
        moviesRepository.getDiscoverMovies(rxDisposer, genreId)
}