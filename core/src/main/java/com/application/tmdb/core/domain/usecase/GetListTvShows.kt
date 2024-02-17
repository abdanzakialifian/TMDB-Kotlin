package com.application.tmdb.core.domain.usecase

import androidx.paging.PagingData
import com.application.tmdb.core.domain.interfaces.ITvShowsRepository
import com.application.tmdb.core.domain.model.MovieTvShowModel
import com.application.tmdb.core.utils.Page
import com.application.tmdb.core.utils.TvShow
import io.reactivex.Flowable
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetListTvShows @Inject constructor(private val iTvShowsRepository: ITvShowsRepository) {
    operator fun invoke(
        tvShow: TvShow?,
        page: Page?,
        query: String?,
        tvId: Int?
    ): Flowable<PagingData<MovieTvShowModel>> = iTvShowsRepository.getTvShows(tvShow, page, query, tvId)
}