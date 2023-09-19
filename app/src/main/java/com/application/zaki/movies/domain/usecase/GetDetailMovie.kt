package com.application.zaki.movies.domain.usecase

import com.application.zaki.movies.domain.interfaces.IMoviesRepository
import com.application.zaki.movies.domain.model.movies.DetailMovies
import io.reactivex.Flowable
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetDetailMovie @Inject constructor(private val iMoviesRepository: IMoviesRepository) {
    operator fun invoke(movieId: String): Flowable<DetailMovies> =
        iMoviesRepository.getDetailMovies(movieId)
}