package com.application.tmdb.domain.usecase

import androidx.paging.PagingData
import com.application.tmdb.core.utils.Page
import com.application.tmdb.core.utils.TvShow
import com.application.tmdb.domain.interfaces.ITvShowsRepository
import com.application.tmdb.domain.model.MovieTvShowModel
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