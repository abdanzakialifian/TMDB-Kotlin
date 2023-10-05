package com.application.zaki.movies.domain.usecase

import androidx.paging.PagingData
import com.application.zaki.movies.domain.interfaces.IMoviesRepository
import com.application.zaki.movies.domain.model.MovieTvShowModel
import com.application.zaki.movies.utils.Movie
import com.application.zaki.movies.utils.Page
import io.reactivex.Flowable
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetListMovies @Inject constructor(private val iMoviesRepository: IMoviesRepository) {
    operator fun invoke(
        movie: Movie?, page: Page?, query: String?, movieId: Int?
    ): Flowable<PagingData<MovieTvShowModel>> = iMoviesRepository.getMovies(movie, page, query, movieId)
}