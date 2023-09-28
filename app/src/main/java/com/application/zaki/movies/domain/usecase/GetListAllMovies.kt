package com.application.zaki.movies.domain.usecase

import android.util.Log
import androidx.paging.PagingData
import androidx.paging.rxjava2.cachedIn
import com.application.zaki.movies.domain.interfaces.IMoviesRepository
import com.application.zaki.movies.domain.model.MovieTvShow
import com.application.zaki.movies.utils.Movie
import com.application.zaki.movies.utils.Page
import io.reactivex.Flowable
import io.reactivex.functions.Function4
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject
import javax.inject.Singleton

@OptIn(ExperimentalCoroutinesApi::class)
@Singleton
class GetListAllMovies @Inject constructor(private val iMoviesRepository: IMoviesRepository) {
    operator fun invoke(
        nowPlayingMovie: Movie?,
        topRatedMovie: Movie?,
        popularMovie: Movie?,
        upComingMovie: Movie?,
        page: Page?,
        query: String?,
        scope: CoroutineScope
    ): Flowable<List<Pair<Movie, PagingData<MovieTvShow>>>> {
        return Flowable.zip(
            iMoviesRepository.getMovies(nowPlayingMovie, page, query).cachedIn(scope),
            iMoviesRepository.getMovies(topRatedMovie, page, query).cachedIn(scope),
            iMoviesRepository.getMovies(popularMovie, page, query).cachedIn(scope),
            iMoviesRepository.getMovies(upComingMovie, page, query).cachedIn(scope),
            Function4 { nowPlayingMoviePaging, topRatedMoviePaging, popularMoviePaging, upComingMoviePaging ->
                return@Function4 listOf(
                    Pair(Movie.NOW_PLAYING_MOVIES, nowPlayingMoviePaging),
                    Pair(Movie.TOP_RATED_MOVIES, topRatedMoviePaging),
                    Pair(Movie.POPULAR_MOVIES, popularMoviePaging),
                    Pair(Movie.UP_COMING_MOVIES, upComingMoviePaging)
                )
            })
    }
}