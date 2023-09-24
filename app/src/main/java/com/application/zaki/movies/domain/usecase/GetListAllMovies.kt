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
import io.reactivex.functions.Function4
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject
import javax.inject.Singleton

@OptIn(ExperimentalCoroutinesApi::class)
@Singleton
class GetListAllMovies @Inject constructor(private val iMoviesRepository: IMoviesRepository) {
    operator fun invoke(
        nowPlayingMovie: Movie,
        topRatedMovie: Movie,
        popularMovie: Movie,
        upComingMovie: Movie,
        category: Category,
        page: Page,
        scope: CoroutineScope
    ): Flowable<List<Pair<Movie, PagingData<MovieTvShow>>>> {
        val nowPlayingMovieFlowable = Flowable.zip(
            iMoviesRepository.getMovies(nowPlayingMovie, page).cachedIn(scope),
            iMoviesRepository.getGenres(category),
        ) { movies, genres ->
            return@zip movies.map { map ->
                map.toMovieTvShowWithGenres(genres)
            }
        }

        val topRatedMovieFlowable = Flowable.zip(
            iMoviesRepository.getMovies(topRatedMovie, page).cachedIn(scope),
            iMoviesRepository.getGenres(category),
        ) { movies, genres ->
            return@zip movies.map { map ->
                map.toMovieTvShowWithGenres(genres)
            }
        }

        val popularMovieFlowable = Flowable.zip(
            iMoviesRepository.getMovies(popularMovie, page).cachedIn(scope),
            iMoviesRepository.getGenres(category),
        ) { movies, genres ->
            return@zip movies.map { map ->
                map.toMovieTvShowWithGenres(genres)
            }
        }

        val upComingMovieFlowable = Flowable.zip(
            iMoviesRepository.getMovies(upComingMovie, page).cachedIn(scope),
            iMoviesRepository.getGenres(category),
        ) { movies, genres ->
            return@zip movies.map { map ->
                map.toMovieTvShowWithGenres(genres)
            }
        }

        return Flowable.zip(nowPlayingMovieFlowable,
            topRatedMovieFlowable,
            popularMovieFlowable,
            upComingMovieFlowable,
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