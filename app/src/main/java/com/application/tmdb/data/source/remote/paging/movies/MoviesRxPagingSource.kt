package com.application.tmdb.data.source.remote.paging.movies

import androidx.paging.PagingState
import androidx.paging.rxjava2.RxPagingSource
import com.application.tmdb.data.source.remote.ApiService
import com.application.tmdb.data.source.remote.response.movies.ListMoviesResponse
import com.application.tmdb.data.source.remote.response.movies.MoviesResponse
import com.application.tmdb.utils.Movie
import com.application.tmdb.utils.Page
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class MoviesRxPagingSource @Inject constructor(private val apiService: ApiService) :
    RxPagingSource<Int, ListMoviesResponse>() {

    private var page: Page? = null

    private var movie: Movie? = null

    private var query: String? = null

    private var movieId: Int? = null

    override fun loadSingle(params: LoadParams<Int>): Single<LoadResult<Int, ListMoviesResponse>> {
        val position = if (page == Page.ONE) {
            INITIAL_POSITION
        } else {
            params.key ?: INITIAL_POSITION
        }

        return if (query != null && query?.isNotEmpty() == true) {
            apiService.getSearchMovies(query ?: "", position)
                .subscribeOn(Schedulers.io())
                .map { data ->
                    toLoadResult(data, position)
                }.onErrorReturn { throwable ->
                    LoadResult.Error(throwable)
                }
        } else if (movieId != null) {
            apiService.getSimilarMovies(movieId ?: 0, position)
                .subscribeOn(Schedulers.io())
                .map { data ->
                    toLoadResult(data, position)
                }.onErrorReturn { throwable ->
                    LoadResult.Error(throwable)
                }
        } else {
            when (movie) {
                Movie.NOW_PLAYING_MOVIES -> apiService.getNowPlayingMovies(position)
                    .subscribeOn(Schedulers.io())
                    .map { data ->
                        toLoadResult(data, position)
                    }.onErrorReturn { throwable ->
                        LoadResult.Error(throwable)
                    }

                Movie.POPULAR_MOVIES -> apiService.getPopularMoviesPaging(position)
                    .subscribeOn(Schedulers.io())
                    .map { data ->
                        toLoadResult(data, position)
                    }.onErrorReturn { throwable ->
                        LoadResult.Error(throwable)
                    }

                Movie.TOP_RATED_MOVIES -> apiService.getTopRatedMoviesPaging(position)
                    .subscribeOn(Schedulers.io())
                    .map { data ->
                        toLoadResult(data, position)
                    }.onErrorReturn { throwable ->
                        LoadResult.Error(throwable)
                    }

                else -> apiService.getUpComingMoviesPaging(position)
                    .subscribeOn(Schedulers.io())
                    .map { data ->
                        toLoadResult(data, position)
                    }.onErrorReturn { throwable ->
                        LoadResult.Error(throwable)
                    }
            }
        }
    }

    private fun toLoadResult(
        data: MoviesResponse, position: Int
    ): LoadResult<Int, ListMoviesResponse> {
        return LoadResult.Page(
            data = data.results ?: emptyList(),
            prevKey = if (position == 1) null else position - 1,
            nextKey = if (page != Page.ONE) {
                if (position == data.totalPages) null else position + 1
            } else {
                null
            }
        )
    }

    override fun getRefreshKey(state: PagingState<Int, ListMoviesResponse>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    fun setData(movie: Movie?, page: Page?, query: String?, movieId: Int?) {
        this.movie = movie
        this.page = page
        this.query = query
        this.movieId = movieId
    }

    companion object {
        private const val INITIAL_POSITION = 1
    }
}