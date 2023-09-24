package com.application.zaki.movies.domain.usecase

import androidx.paging.PagingData
import androidx.paging.map
import androidx.paging.rxjava2.cachedIn
import com.application.zaki.movies.domain.interfaces.IMoviesRepository
import com.application.zaki.movies.domain.model.MovieTvShow
import com.application.zaki.movies.utils.Category
import com.application.zaki.movies.utils.DataMapper.toMovieTvShowWithGenres
import com.application.zaki.movies.utils.Movie
import com.application.zaki.movies.utils.Page
import io.reactivex.Flowable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject
import javax.inject.Singleton

@OptIn(ExperimentalCoroutinesApi::class)
@Singleton
class GetListMovies @Inject constructor(private val iMoviesRepository: IMoviesRepository) {
    operator fun invoke(
        movie: Movie,
        category: Category,
        page: Page,
        scope: CoroutineScope
    ): Flowable<PagingData<MovieTvShow>> = Flowable.zip(
        iMoviesRepository.getMovies(movie, page).cachedIn(scope),
        iMoviesRepository.getGenres(category),
    ) { movies, genres ->
        return@zip movies.map { map ->
            map.toMovieTvShowWithGenres(genres)
        }
    }
}