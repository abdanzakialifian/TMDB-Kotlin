package com.application.tmdb.core.domain.usecase

import androidx.paging.PagingData
import androidx.paging.rxjava2.cachedIn
import com.application.tmdb.core.domain.interfaces.IMoviesRepository
import com.application.tmdb.core.domain.model.MovieTvShowModel
import com.application.tmdb.common.Movie
import com.application.tmdb.common.Page
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
        nowPlayingMovie: com.application.tmdb.common.Movie?,
        topRatedMovie: com.application.tmdb.common.Movie?,
        popularMovie: com.application.tmdb.common.Movie?,
        upComingMovie: com.application.tmdb.common.Movie?,
        page: com.application.tmdb.common.Page?,
        query: String?,
        movieId: Int?,
        scope: CoroutineScope
    ): Flowable<List<Pair<com.application.tmdb.common.Movie, PagingData<MovieTvShowModel>>>> {
        return Flowable.zip(
            iMoviesRepository.getMovies(nowPlayingMovie, page, query, movieId).cachedIn(scope),
            iMoviesRepository.getMovies(topRatedMovie, page, query, movieId).cachedIn(scope),
            iMoviesRepository.getMovies(popularMovie, page, query, movieId).cachedIn(scope),
            iMoviesRepository.getMovies(upComingMovie, page, query, movieId).cachedIn(scope),
            Function4 { nowPlayingMoviePaging, topRatedMoviePaging, popularMoviePaging, upComingMoviePaging ->
                return@Function4 listOf(
                    Pair(com.application.tmdb.common.Movie.NOW_PLAYING_MOVIES, nowPlayingMoviePaging),
                    Pair(com.application.tmdb.common.Movie.TOP_RATED_MOVIES, topRatedMoviePaging),
                    Pair(com.application.tmdb.common.Movie.POPULAR_MOVIES, popularMoviePaging),
                    Pair(com.application.tmdb.common.Movie.UP_COMING_MOVIES, upComingMoviePaging)
                )
            })
    }
}