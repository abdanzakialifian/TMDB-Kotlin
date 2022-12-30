package com.application.zaki.movies.data.source.remote.paging.tvshows

import androidx.paging.PagingState
import androidx.paging.rxjava2.RxPagingSource
import com.application.zaki.movies.data.source.remote.ApiService
import com.application.zaki.movies.data.source.remote.response.tvshows.ListTopRatedTvShowsResponse
import com.application.zaki.movies.data.source.remote.response.tvshows.TopRatedTvShowsResponse
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class TopRatedTvShowsRxPagingSource @Inject constructor(private val apiService: ApiService) :
    RxPagingSource<Int, ListTopRatedTvShowsResponse>() {
    override fun loadSingle(params: LoadParams<Int>): Single<LoadResult<Int, ListTopRatedTvShowsResponse>> {
        val position = params.key ?: 1

        return apiService.getTopRatedTvShowsPaging(position)
            .subscribeOn(Schedulers.io())
            .map {
                toLoadResult(it, position)
            }
            .onErrorReturn {
                LoadResult.Error(it)
            }
    }

    private fun toLoadResult(
        data: TopRatedTvShowsResponse,
        position: Int
    ): LoadResult<Int, ListTopRatedTvShowsResponse> {
        val listTopRatedTvShowsResponse = ArrayList<ListTopRatedTvShowsResponse>()

        data.results?.map { map ->
            map?.let {
                listTopRatedTvShowsResponse.add(it)
            }
        }

        return LoadResult.Page(
            data = listTopRatedTvShowsResponse,
            prevKey = if (position == 1) null else position - 1,
            nextKey = if (position == data.totalPages) null else position + 1
        )
    }

    override fun getRefreshKey(state: PagingState<Int, ListTopRatedTvShowsResponse>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }
}