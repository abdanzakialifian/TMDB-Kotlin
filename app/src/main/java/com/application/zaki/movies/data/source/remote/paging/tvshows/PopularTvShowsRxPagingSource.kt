package com.application.zaki.movies.data.source.remote.paging.tvshows

import androidx.paging.PagingState
import androidx.paging.rxjava2.RxPagingSource
import com.application.zaki.movies.data.source.remote.ApiService
import com.application.zaki.movies.data.source.remote.response.tvshows.ListPopularTvShowsResponse
import com.application.zaki.movies.data.source.remote.response.tvshows.PopularTvShowsResponse
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class PopularTvShowsRxPagingSource @Inject constructor(private val apiService: ApiService) :
    RxPagingSource<Int, ListPopularTvShowsResponse>() {
    override fun loadSingle(params: LoadParams<Int>): Single<LoadResult<Int, ListPopularTvShowsResponse>> {
        val position = params.key ?: 1

        return apiService.getPopularTvShowsPaging(position)
            .subscribeOn(Schedulers.io())
            .map {
                toLoadResult(it, position)
            }
            .onErrorReturn {
                LoadResult.Error(it)
            }
    }

    private fun toLoadResult(
        data: PopularTvShowsResponse,
        position: Int
    ): LoadResult<Int, ListPopularTvShowsResponse> {
        val listPopularTvShowsResponse = ArrayList<ListPopularTvShowsResponse>()

        data.results?.map { map ->
            map?.let {
                listPopularTvShowsResponse.add(it)
            }
        }

        return LoadResult.Page(
            data = listPopularTvShowsResponse,
            prevKey = if (position == 1) null else position - 1,
            nextKey = if (position == data.totalPages) null else position + 1
        )
    }

    override fun getRefreshKey(state: PagingState<Int, ListPopularTvShowsResponse>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }
}