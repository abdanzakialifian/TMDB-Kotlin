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

    private var totalPage = ""

    override fun loadSingle(params: LoadParams<Int>): Single<LoadResult<Int, ListPopularTvShowsResponse>> {
        val position = if (totalPage == ONE) {
            1
        } else {
            params.key ?: 1
        }

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
            nextKey = if (totalPage != ONE) if (position == data.totalPages) null else position + 1 else null
        )
    }

    override fun getRefreshKey(state: PagingState<Int, ListPopularTvShowsResponse>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    fun setTotalPage(totalPage: String) {
        this.totalPage = totalPage
    }

    companion object {
        const val ONE = "ONE"
        const val MORE_THAN_ONE = "MORE THAN ONE"
    }
}