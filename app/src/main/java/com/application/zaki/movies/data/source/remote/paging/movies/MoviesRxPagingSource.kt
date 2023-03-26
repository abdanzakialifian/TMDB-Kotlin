package com.application.zaki.movies.data.source.remote.paging.movies

import androidx.paging.PagingState
import androidx.paging.rxjava2.RxPagingSource
import com.application.zaki.movies.data.source.remote.ApiService
import com.application.zaki.movies.data.source.remote.response.movies.ListMoviesResponse
import com.application.zaki.movies.data.source.remote.response.movies.MoviesResponse
import com.application.zaki.movies.utils.Movie
import com.application.zaki.movies.utils.Page
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class MoviesRxPagingSource @Inject constructor(private val apiService: ApiService) :
    RxPagingSource<Int, ListMoviesResponse>() {

    private lateinit var page: Page
    private lateinit var movie: Movie

    override fun loadSingle(params: LoadParams<Int>): Single<LoadResult<Int, ListMoviesResponse>> {
        val position = if (page == Page.ONE) {
            1
        } else {
            params.key ?: 1
        }

        return when (movie) {
            Movie.NOW_PLAYING_MOVIES -> apiService.getNowPlayingMovies(position)
                .subscribeOn(Schedulers.io())
                .map {
                    toLoadResult(it, position)
                }.onErrorReturn {
                    LoadResult.Error(it)
                }
            Movie.POPULAR_MOVIES -> apiService.getPopularMoviesPaging(position)
                .subscribeOn(Schedulers.io())
                .map {
                    toLoadResult(it, position)
                }.onErrorReturn {
                    LoadResult.Error(it)
                }
            Movie.TOP_RATED_MOVIES -> apiService.getTopRatedMoviesPaging(position)
                .subscribeOn(Schedulers.io())
                .map {
                    toLoadResult(it, position)
                }.onErrorReturn {
                    LoadResult.Error(it)
                }
            Movie.UP_COMING_MOVIES -> apiService.getUpComingMoviesPaging(position)
                .subscribeOn(Schedulers.io())
                .map {
                    toLoadResult(it, position)
                }.onErrorReturn {
                    LoadResult.Error(it)
                }
        }
    }

    private fun toLoadResult(
        data: MoviesResponse, position: Int
    ): LoadResult<Int, ListMoviesResponse> {
        return LoadResult.Page(
            data = data.results ?: emptyList(),
            prevKey = if (position == 1) null else position - 1,
            nextKey = if (page != Page.ONE) if (position == data.totalPages) null else position + 1 else null
        )
    }

    override fun getRefreshKey(state: PagingState<Int, ListMoviesResponse>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    fun setData(movie: Movie, page: Page) {
        this.movie = movie
        this.page = page
    }
}