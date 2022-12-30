package com.application.zaki.movies.data.source.remote.paging.movies

import androidx.paging.PagingState
import androidx.paging.rxjava2.RxPagingSource
import com.application.zaki.movies.data.source.remote.ApiService
import com.application.zaki.movies.data.source.remote.response.movies.ListPopularMoviesResponse
import com.application.zaki.movies.data.source.remote.response.movies.PopularMoviesResponse
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PopularMoviesRxPagingSource @Inject constructor(private val apiService: ApiService) :
    RxPagingSource<Int, ListPopularMoviesResponse>() {

    override fun loadSingle(params: LoadParams<Int>): Single<LoadResult<Int, ListPopularMoviesResponse>> {
        val position = params.key ?: 1

        return apiService.getPopularMoviesPaging(position)
            .subscribeOn(Schedulers.io())
            .map {
                toLoadResult(it, position)
            }
            .onErrorReturn {
                LoadResult.Error(it)
            }
    }

    private fun toLoadResult(
        data: PopularMoviesResponse,
        position: Int
    ): LoadResult<Int, ListPopularMoviesResponse> {
        // mapping list because list is nullable
        val listPopularMoviesResponse = ArrayList<ListPopularMoviesResponse>()
        data.results?.map { map ->
            map?.let {
                listPopularMoviesResponse.add(it)
            }
        }

        return LoadResult.Page(
            // not nullable list
            data = listPopularMoviesResponse,
            prevKey = if (position == 1) null else position - 1,
            nextKey = if (position == data.totalPages) null else position + 1
        )
    }

    override fun getRefreshKey(state: PagingState<Int, ListPopularMoviesResponse>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }
}