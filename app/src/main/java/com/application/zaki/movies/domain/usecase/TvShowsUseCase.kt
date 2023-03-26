package com.application.zaki.movies.domain.usecase

import androidx.paging.PagingData
import androidx.paging.map
import com.application.zaki.movies.domain.interfaces.ITvShowsRepository
import com.application.zaki.movies.domain.interfaces.ITvShowsUseCase
import com.application.zaki.movies.domain.model.tvshows.DetailTvShows
import com.application.zaki.movies.domain.model.tvshows.ListTvShows
import com.application.zaki.movies.utils.Genre
import com.application.zaki.movies.utils.Page
import com.application.zaki.movies.utils.TvShow
import io.reactivex.Flowable
import javax.inject.Inject

class TvShowsUseCase @Inject constructor(private val tvShowsRepository: ITvShowsRepository) :
    ITvShowsUseCase {
    override fun getDetailTvShows(tvId: String): Flowable<DetailTvShows> =
        tvShowsRepository.getDetailTvShows(tvId)

    override fun getTvShows(
        tvShow: TvShow,
        genre: Genre,
        page: Page
    ): Flowable<PagingData<ListTvShows>> = Flowable.zip(
        tvShowsRepository.getTvShows(tvShow, page),
        tvShowsRepository.getGenres(genre)
    ) { tvShows, genres ->
        return@zip tvShows.map { map ->
            map.
        }
    }
}