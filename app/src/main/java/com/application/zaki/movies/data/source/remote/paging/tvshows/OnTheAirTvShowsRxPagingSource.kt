package com.application.zaki.movies.data.source.remote.paging.tvshows

import androidx.paging.PagingState
import androidx.paging.rxjava2.RxPagingSource
import com.application.zaki.movies.data.source.remote.ApiService
import com.application.zaki.movies.data.source.remote.response.tvshows.ListOnTheAirTvShowsResponse
import com.application.zaki.movies.data.source.remote.response.tvshows.OnTheAirTvShowsResponse
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class OnTheAirTvShowsRxPagingSource @Inject constructor(private val apiService: ApiService) :
    RxPagingSource<Int, ListOnTheAirTvShowsResponse>() {
    override fun loadSingle(params: LoadParams<Int>): Single<LoadResult<Int, ListOnTheAirTvShowsResponse>> {
        val position = params.key ?: 1

        return apiService.getOnTheAirTvShowsPaging(position)
            .subscribeOn(Schedulers.io())
            .map {
                toLoadResult(it, position)
            }
            .onErrorReturn {
                LoadResult.Error(it)
            }
    }

    private fun toLoadResult(
        data: OnTheAirTvShowsResponse,
        position: Int
    ): LoadResult<Int, ListOnTheAirTvShowsResponse> {
        val listOnTheAirTvShowsResponse = ArrayList<ListOnTheAirTvShowsResponse>()

        data.results?.map { map ->
            map?.let {
                listOnTheAirTvShowsResponse.add(it)
            }
        }

        return LoadResult.Page(
            data = listOnTheAirTvShowsResponse,
            prevKey = if (position == 1) null else position - 1,
            nextKey = if (position == data.totalPages) null else position + 1
        )
    }

    override fun getRefreshKey(state: PagingState<Int, ListOnTheAirTvShowsResponse>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }
}