package com.application.zaki.movies.domain.usecase

import androidx.paging.PagingData
import com.application.zaki.movies.domain.interfaces.ITvShowsRepository
import com.application.zaki.movies.domain.model.MovieTvShow
import com.application.zaki.movies.utils.Page
import com.application.zaki.movies.utils.TvShow
import io.reactivex.Flowable
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetListTvShows @Inject constructor(private val iTvShowsRepository: ITvShowsRepository) {
    operator fun invoke(
        tvShow: TvShow?,
        page: Page?,
        query: String?,
    ): Flowable<PagingData<MovieTvShow>> = iTvShowsRepository.getTvShows(tvShow, page, query)
}