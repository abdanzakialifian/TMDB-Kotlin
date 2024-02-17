package com.application.tmdb.domain.usecase

import com.application.tmdb.domain.interfaces.ITvShowsRepository
import com.application.tmdb.common.model.DetailModel
import io.reactivex.Flowable
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetDetailTvShow @Inject constructor(private val iTvShowsRepository: ITvShowsRepository) {
    operator fun invoke(tvId: String): Flowable<DetailModel> =
        iTvShowsRepository.getDetailTvShows(tvId)
}