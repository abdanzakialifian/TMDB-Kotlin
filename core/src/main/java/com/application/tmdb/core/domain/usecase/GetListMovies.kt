package com.application.tmdb.core.domain.usecase

import androidx.paging.PagingData
import com.application.tmdb.core.domain.interfaces.IMoviesRepository
import com.application.tmdb.core.domain.model.MovieTvShowModel
import com.application.tmdb.common.Movie
import com.application.tmdb.common.Page
import io.reactivex.Flowable
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetListMovies @Inject constructor(private val iMoviesRepository: IMoviesRepository) {
    operator fun invoke(
        movie: com.application.tmdb.common.Movie?, page: com.application.tmdb.common.Page?, query: String?, movieId: Int?
    ): Flowable<PagingData<MovieTvShowModel>> = iMoviesRepository.getMovies(movie, page, query, movieId)
}