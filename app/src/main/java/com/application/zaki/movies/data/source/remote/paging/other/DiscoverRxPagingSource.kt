package com.application.zaki.movies.data.source.remote.paging.other

import androidx.paging.PagingState
import androidx.paging.rxjava2.RxPagingSource
import com.application.zaki.movies.data.source.remote.ApiService
import com.application.zaki.movies.data.source.remote.response.other.DiscoverResponse
import com.application.zaki.movies.data.source.remote.response.other.ResultsItemDiscoverResponse
import com.application.zaki.movies.utils.Category
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DiscoverRxPagingSource @Inject constructor(private val apiService: ApiService) :
    RxPagingSource<Int, ResultsItemDiscoverResponse>() {

    private var genreId: String = ""
    private var category: Category = Category.MOVIES

    override fun loadSingle(params: LoadParams<Int>): Single<LoadResult<Int, ResultsItemDiscoverResponse>> {
        val position = params.key ?: 1

        return when (category) {
            Category.MOVIES -> apiService.getDiscoverMovie(position, genreId)
                .subscribeOn(Schedulers.io())
                .map { data ->
                    toLoadResult(data, position)
                }.onErrorReturn { throwable ->
                    LoadResult.Error(throwable)
                }

            Category.TV_SHOWS -> apiService.getDiscoverTvShow(position, genreId)
                .subscribeOn(Schedulers.io())
                .map { data ->
                    toLoadResult(data, position)
                }.onErrorReturn { throwable ->
                    LoadResult.Error(throwable)
                }
        }
    }

    private fun toLoadResult(
        data: DiscoverResponse, position: Int
    ): LoadResult<Int, ResultsItemDiscoverResponse> {
        return LoadResult.Page(
            data = data.results ?: emptyList(),
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

    fun setDataDiscover(genreId: String, category: Category) {
        this.genreId = genreId
        this.category = category
    }
}