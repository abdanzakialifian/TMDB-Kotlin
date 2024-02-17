package com.application.tmdb.domain.usecase

import com.application.tmdb.domain.interfaces.IMoviesRepository
import com.application.tmdb.common.model.DetailModel
import io.reactivex.Flowable
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetDetailMovie @Inject constructor(private val iMoviesRepository: IMoviesRepository) {
    operator fun invoke(movieId: String): Flowable<DetailModel> =
        iMoviesRepository.getDetailMovies(movieId)
}