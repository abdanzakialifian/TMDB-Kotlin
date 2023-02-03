package com.application.zaki.movies.data.source.remote.paging.combine

import androidx.paging.PagingState
import androidx.paging.rxjava2.RxPagingSource
import com.application.zaki.movies.data.source.remote.ApiService
import com.application.zaki.movies.data.source.remote.response.combine.DiscoverResponse
import com.application.zaki.movies.data.source.remote.response.combine.ResultsItemDiscoverResponse
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DiscoverRxPagingSource @Inject constructor(private val apiService: ApiService) :
    RxPagingSource<Int, ResultsItemDiscoverResponse>() {

    private var genreId: String = ""
    private var type: String = ""

    override fun loadSingle(params: LoadParams<Int>): Single<LoadResult<Int, ResultsItemDiscoverResponse>> {
        val position = params.key ?: 1

        return if (type == MOVIES) {
            apiService.getDiscoverMovie(position, genreId)
                .subscribeOn(Schedulers.io())
                .map {
                    toLoadResult(it, position)
                }
                .onErrorReturn {
                    LoadResult.Error(it)
                }
        } else {
            apiService.getDiscoverTvShow(position, genreId)
                .subscribeOn(Schedulers.io())
                .map {
                    toLoadResult(it, position)
                }
                .onErrorReturn {
                    LoadResult.Error(it)
                }
        }
    }

    private fun toLoadResult(
        data: DiscoverResponse,
        position: Int
    ): LoadResult<Int, ResultsItemDiscoverResponse> {
        // mapping list because list is nullable
        val listDiscoverResponse = ArrayList<ResultsItemDiscoverResponse>()
        data.results?.map { map ->
            map?.let {
                listDiscoverResponse.add(it)
            }
        }

        return LoadResult.Page(
            // not nullable list
            data = listDiscoverResponse,
            prevKey = if (position == 1) null else position - 1,
            nextKey = if (position == data.totalPages) null else position + 1
        )
    }

    override fun getRefreshKey(state: PagingState<Int, ResultsItemDiscoverResponse>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    fun setDataDiscover(genreId: String, type: String) {
        this.genreId = genreId
        this.type = type
    }

    companion object {
        const val MOVIES = "MOVIES"
        const val TV_SHOWS = "TV SHOWS"
    }
}