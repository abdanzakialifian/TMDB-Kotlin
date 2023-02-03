package com.application.zaki.movies.data.source.remote.paging.movies

import androidx.paging.PagingState
import androidx.paging.rxjava2.RxPagingSource
import com.application.zaki.movies.data.source.remote.ApiService
import com.application.zaki.movies.data.source.remote.response.movies.ListUpComingMoviesResponse
import com.application.zaki.movies.data.source.remote.response.movies.UpComingMoviesResponse
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class UpComingMoviesRxPagingSource @Inject constructor(private val apiService: ApiService) :
    RxPagingSource<Int, ListUpComingMoviesResponse>() {

    private var totalPage = ""
    override fun loadSingle(params: LoadParams<Int>): Single<LoadResult<Int, ListUpComingMoviesResponse>> {
        val position = if (totalPage == ONE) {
            1
        } else {
            params.key ?: 1
        }

        return apiService.getUpComingMoviesPaging(position)
            .subscribeOn(Schedulers.io())
            .map {
                toLoadResult(it, position)
            }
            .onErrorReturn {
                LoadResult.Error(it)
            }
    }

    private fun toLoadResult(
        data: UpComingMoviesResponse,
        position: Int
    ): LoadResult<Int, ListUpComingMoviesResponse> {
        val listUpComingMoviesResponse = ArrayList<ListUpComingMoviesResponse>()

        data.results?.map { map ->
            map?.let {
                listUpComingMoviesResponse.add(it)
            }
        }

        return LoadResult.Page(
            data = listUpComingMoviesResponse,
            prevKey = if (position == 1) null else -1,
            nextKey = if (totalPage != ONE) if (position == data.totalPages) null else position + 1 else null
        )
    }

    override fun getRefreshKey(state: PagingState<Int, ListUpComingMoviesResponse>): Int? {
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