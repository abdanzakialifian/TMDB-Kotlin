package com.application.zaki.movies.domain.usecase

import androidx.paging.map
import com.application.zaki.movies.domain.interfaces.IMoviesRepository
import com.application.zaki.movies.utils.Category
import com.application.zaki.movies.utils.DataMapper.toMovieTvShowWithGenres
import com.application.zaki.movies.utils.Movie
import com.application.zaki.movies.utils.Page
import io.reactivex.Flowable
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetListMovies @Inject constructor(private val iMoviesRepository: IMoviesRepository) {
    operator fun invoke(
        movie: Movie,
        category: Category,
        page: Page
    ) = Flowable.zip(
        iMoviesRepository.getMovies(movie, page),
        iMoviesRepository.getGenres(category),
    ) { movies, genres ->
        return@zip movies.map { map ->
            map.toMovieTvShowWithGenres(genres)
        }
    }
}