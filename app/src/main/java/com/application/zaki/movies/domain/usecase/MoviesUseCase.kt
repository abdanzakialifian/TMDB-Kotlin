package com.application.zaki.movies.domain.usecase

import androidx.paging.PagingData
import com.application.zaki.movies.domain.interfaces.IMoviesRepository
import com.application.zaki.movies.domain.interfaces.IMoviesUseCase
import com.application.zaki.movies.domain.model.movies.*
import io.reactivex.Flowable
import javax.inject.Inject

class MoviesUseCase @Inject constructor(private val moviesRepository: IMoviesRepository) :
    IMoviesUseCase {
    override fun getNowPlayingMovies(): Flowable<NowPlayingMovies> =
        moviesRepository.getNowPlayingMovies()

    override fun getDetailMovies(movieId: String): Flowable<DetailMovies> =
        moviesRepository.getDetailMovies(movieId)

    override fun getPopularMoviesPaging(type: String, totalPage: String): Flowable<PagingData<ListPopularMovies>> =
        moviesRepository.getPopularMoviesPaging(type, totalPage)

    override fun getTopRatedMoviesPaging(type: String, totalPage: String): Flowable<PagingData<ListTopRatedMovies>> =
        moviesRepository.getTopRatedMoviesPaging(type, totalPage)

    override fun getUpComingMoviesPaging(type: String, totalPage: String): Flowable<PagingData<ListUpComingMovies>> =
        moviesRepository.getUpComingMoviesPaging(type, totalPage)
}