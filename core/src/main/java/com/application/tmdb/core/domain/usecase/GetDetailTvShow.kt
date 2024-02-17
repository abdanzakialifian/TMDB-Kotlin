package com.application.tmdb.core.domain.usecase

import com.application.tmdb.core.domain.interfaces.ITvShowsRepository
import com.application.tmdb.core.domain.model.DetailModel
import io.reactivex.Flowable
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetDetailTvShow @Inject constructor(private val iTvShowsRepository: ITvShowsRepository) {
    operator fun invoke(tvId: String): Flowable<DetailModel> =
        iTvShowsRepository.getDetailTvShows(tvId)
}