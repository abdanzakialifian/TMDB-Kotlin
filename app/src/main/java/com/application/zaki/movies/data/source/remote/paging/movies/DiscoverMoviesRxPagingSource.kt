package com.application.zaki.movies.data.source.remote.paging.movies

import androidx.paging.PagingState
import androidx.paging.rxjava2.RxPagingSource
import com.application.zaki.movies.data.source.remote.ApiService
import com.application.zaki.movies.data.source.remote.response.movies.DiscoverMovieResponse
import com.application.zaki.movies.data.source.remote.response.movies.ResultsItemDiscoverResponse
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DiscoverMoviesRxPagingSource @Inject constructor(private val apiService: ApiService) :
    RxPagingSource<Int, ResultsItemDiscoverResponse>() {
    private var genreId: String = ""

    override fun loadSingle(params: LoadParams<Int>): Single<LoadResult<Int, ResultsItemDiscoverResponse>> {
        val position = params.key ?: 1

        return apiService.getDiscoverMovie(position, genreId)
            .subscribeOn(Schedulers.io())
            .map {
                toLoadResult(it, position)
            }
            .onErrorReturn {
                LoadResult.Error(it)
            }
    }

    private fun toLoadResult(
        data: DiscoverMovieResponse,
        position: Int
    ): LoadResult<Int, ResultsItemDiscoverResponse> {
        // mapping list because list is nullable
        val listDiscoverMoviesResponse = ArrayList<ResultsItemDiscoverResponse>()
        data.results?.map { map ->
            map?.let {
                listDiscoverMoviesResponse.add(it)
            }
        }

        return LoadResult.Page(
            // not nullable list
            data = listDiscoverMoviesResponse,
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

    fun getGenreId(genreId: String) {
        this.genreId = genreId
    }
}