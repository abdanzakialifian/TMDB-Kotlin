package com.application.tmdb.domain.usecase

import androidx.paging.PagingData
import com.application.tmdb.common.model.MovieTvShowModel
import com.application.tmdb.common.utils.Movie
import com.application.tmdb.common.utils.Page
import com.application.tmdb.domain.interfaces.IMoviesRepository
import io.reactivex.Flowable
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetListMovies @Inject constructor(private val iMoviesRepository: IMoviesRepository) {
    operator fun invoke(
        movie: Movie?, page: Page?, query: String?, movieId: Int?
    ): Flowable<PagingData<MovieTvShowModel>> = iMoviesRepository.getMovies(movie, page, query, movieId)
}