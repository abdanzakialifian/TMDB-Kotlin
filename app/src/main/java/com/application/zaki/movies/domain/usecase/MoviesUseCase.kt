package com.application.zaki.movies.domain.usecase

import androidx.paging.PagingData
import com.application.zaki.movies.domain.interfaces.IMoviesRepository
import com.application.zaki.movies.domain.interfaces.IMoviesUseCase
import com.application.zaki.movies.domain.model.movies.*
import com.application.zaki.movies.utils.Genre
import com.application.zaki.movies.utils.Movie
import com.application.zaki.movies.utils.Page
import io.reactivex.Flowable
import javax.inject.Inject

class MoviesUseCase @Inject constructor(private val moviesRepository: IMoviesRepository) :
    IMoviesUseCase {
    override fun getDetailMovies(movieId: String): Flowable<DetailMovies> =
        moviesRepository.getDetailMovies(movieId)

    override fun getMovies(
        movie: Movie,
        genre: Genre,
        page: Page
    ): Flowable<PagingData<ListMovies>> = moviesRepository.getMovies(movie, genre, page)
}