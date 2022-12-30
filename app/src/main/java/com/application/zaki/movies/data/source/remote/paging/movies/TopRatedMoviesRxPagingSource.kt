package com.application.zaki.movies.data.source.remote.paging.movies

import androidx.paging.PagingState
import androidx.paging.rxjava2.RxPagingSource
import com.application.zaki.movies.data.source.remote.ApiService
import com.application.zaki.movies.data.source.remote.response.movies.ListTopRatedMoviesResponse
import com.application.zaki.movies.data.source.remote.response.movies.TopRatedMoviesResponse
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
data class TopRatedMoviesRxPagingSource @Inject constructor(private val apiService: ApiService) :
    RxPagingSource<Int, ListTopRatedMoviesResponse>() {

    override fun loadSingle(params: LoadParams<Int>): Single<LoadResult<Int, ListTopRatedMoviesResponse>> {
        val position = params.key ?: 1

        return apiService.getTopRatedMoviesPaging(position)
            .subscribeOn(Schedulers.io())
            .map {
                toLoadResult(it, position)
            }
            .onErrorReturn {
                LoadResult.Error(it)
            }
    }

    private fun toLoadResult(
        data: TopRatedMoviesResponse,
        position: Int
    ): LoadResult<Int, ListTopRatedMoviesResponse> {
        val listTopRatedMoviesResponse = ArrayList<ListTopRatedMoviesResponse>()

        data.results?.map { map ->
            map?.let {
                listTopRatedMoviesResponse.add(it)
            }
        }

        return LoadResult.Page(
            data = listTopRatedMoviesResponse,
            prevKey = if (position == 1) null else position - 1,
            nextKey = if (position == data.totalPages) null else position + 1
        )
    }

    override fun getRefreshKey(state: PagingState<Int, ListTopRatedMoviesResponse>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }
}
