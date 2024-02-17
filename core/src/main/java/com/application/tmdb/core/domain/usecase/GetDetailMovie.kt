package com.application.tmdb.core.domain.usecase

import com.application.tmdb.core.domain.interfaces.IMoviesRepository
import com.application.tmdb.core.domain.model.DetailModel
import io.reactivex.Flowable
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetDetailMovie @Inject constructor(private val iMoviesRepository: IMoviesRepository) {
    operator fun invoke(movieId: String): Flowable<DetailModel> =
        iMoviesRepository.getDetailMovies(movieId)
}