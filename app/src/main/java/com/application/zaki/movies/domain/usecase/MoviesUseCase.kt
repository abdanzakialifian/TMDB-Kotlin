package com.application.zaki.movies.domain.usecase

import androidx.paging.PagingData
import com.application.zaki.movies.domain.interfaces.IMoviesRepository
import com.application.zaki.movies.domain.interfaces.IMoviesUseCase
import com.application.zaki.movies.domain.model.movies.*
import com.application.zaki.movies.utils.RxDisposer
import com.application.zaki.movies.utils.UiState
import io.reactivex.Flowable
import javax.inject.Inject

class MoviesUseCase @Inject constructor(private val moviesRepository: IMoviesRepository) :
    IMoviesUseCase {
    override fun getPopularMovies(): Flowable<PopularMovies> = moviesRepository.getPopularMovies()

    override fun getNowPlayingMovies(): Flowable<NowPlayingMovies> =
        moviesRepository.getNowPlayingMovies()

    override fun getTopRatedMovies(): Flowable<TopRatedMovies> =
        moviesRepository.getTopRatedMovies()

    override fun getUpComingMovies(): Flowable<UpComingMovies> =
        moviesRepository.getUpComingMovies()

    override fun getDetailMovies(movieId: String): Flowable<DetailMovies> =
        moviesRepository.getDetailMovies(movieId)

    override fun getPopularMoviesPaging(): Flowable<PagingData<ListPopularMovies>> =
        moviesRepository.getPopularMoviesPaging()

    override fun getTopRatedMoviesPaging(): Flowable<PagingData<ListTopRatedMovies>> =
        moviesRepository.getTopRatedMoviesPaging()

    override fun getUpComingMoviesPaging(): Flowable<PagingData<ListUpComingMovies>> =
        moviesRepository.getUpComingMoviesPaging()

    override fun getReviewsMoviePaging(movieId: String): Flowable<PagingData<ReviewItem>> =
        moviesRepository.getReviewsMoviePaging(movieId)

    override fun getDiscoverMovies(genreId: String): Flowable<PagingData<ResultsItemDiscover>> =
        moviesRepository.getDiscoverMovies(genreId)
}