package com.application.zaki.movies.domain.usecase

import com.application.zaki.movies.domain.interfaces.ITvShowsRepository
import com.application.zaki.movies.domain.model.Detail
import io.reactivex.Flowable
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetDetailTvShow @Inject constructor(private val iTvShowsRepository: ITvShowsRepository) {
    operator fun invoke(tvId: String): Flowable<Detail> =
        iTvShowsRepository.getDetailTvShows(tvId)
}