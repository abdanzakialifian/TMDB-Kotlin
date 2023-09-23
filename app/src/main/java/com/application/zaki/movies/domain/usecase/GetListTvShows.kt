package com.application.zaki.movies.domain.usecase

import androidx.paging.PagingData
import androidx.paging.map
import com.application.zaki.movies.domain.interfaces.ITvShowsRepository
import com.application.zaki.movies.domain.model.MovieTvShow
import com.application.zaki.movies.utils.Category
import com.application.zaki.movies.utils.DataMapper.toMovieTvShowWithGenres
import com.application.zaki.movies.utils.Page
import com.application.zaki.movies.utils.TvShow
import io.reactivex.Flowable
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetListTvShows @Inject constructor(private val iTvShowsRepository: ITvShowsRepository) {
    operator fun invoke(
        tvShow: TvShow,
        category: Category,
        page: Page
    ): Flowable<PagingData<MovieTvShow>> = Flowable.zip(
        iTvShowsRepository.getTvShows(tvShow, page),
        iTvShowsRepository.getGenres(category)
    ) { tvShows, genres ->
        return@zip tvShows.map { map ->
            map.toMovieTvShowWithGenres(genres)
        }
    }
}