package com.application.tmdb.core.domain.usecase

import androidx.paging.PagingData
import com.application.tmdb.core.domain.interfaces.ITvShowsRepository
import com.application.tmdb.core.domain.model.MovieTvShowModel
import com.application.tmdb.common.Page
import com.application.tmdb.common.TvShow
import io.reactivex.Flowable
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetListTvShows @Inject constructor(private val iTvShowsRepository: ITvShowsRepository) {
    operator fun invoke(
        tvShow: com.application.tmdb.common.TvShow?,
        page: com.application.tmdb.common.Page?,
        query: String?,
        tvId: Int?
    ): Flowable<PagingData<MovieTvShowModel>> = iTvShowsRepository.getTvShows(tvShow, page, query, tvId)
}