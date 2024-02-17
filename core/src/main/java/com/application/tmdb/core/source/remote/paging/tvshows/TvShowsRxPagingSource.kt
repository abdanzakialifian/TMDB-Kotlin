package com.application.tmdb.core.source.remote.paging.tvshows

import androidx.paging.PagingState
import androidx.paging.rxjava2.RxPagingSource
import com.application.tmdb.core.utils.Page
import com.application.tmdb.core.utils.TvShow
import com.application.tmdb.core.source.remote.ApiService
import com.application.tmdb.core.source.remote.response.tvshows.ListTvShowsResponse
import com.application.tmdb.core.source.remote.response.tvshows.TvShowsResponse
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class TvShowsRxPagingSource @Inject constructor(private val apiService: ApiService) :
    RxPagingSource<Int, ListTvShowsResponse>() {

    private var page: Page? = null

    private var tvShow: TvShow? = null

    private var query: String? = null

    private var tvId: Int? = null

    override fun loadSingle(params: LoadParams<Int>): Single<LoadResult<Int, ListTvShowsResponse>> {
        val position = if (page == Page.ONE) {
            INITIAL_POSITION
        } else {
            params.key ?: INITIAL_POSITION
        }

        return if (query != null && query?.isNotEmpty() == true) {
            apiService.getSearchTvShows(query ?: "", position)
                .subscribeOn(Schedulers.io())
                .map { data ->
                    toLoadResult(data, position)
                }.onErrorReturn { throwable ->
                    LoadResult.Error(throwable)
                }
        } else if (tvId != null) {
            apiService.getSimilarTvShows(tvId ?: 0, position)
                .subscribeOn(Schedulers.io())
                .map { data ->
                    toLoadResult(data, position)
                }.onErrorReturn { throwable ->
                    LoadResult.Error(throwable)
                }
        } else {
            when (tvShow) {
                TvShow.AIRING_TODAY_TV_SHOWS -> apiService.getAiringTodayTvShows(position)
                    .subscribeOn(
                        Schedulers.io()
                    ).map { data ->
                        toLoadResult(data, position)
                    }.onErrorReturn { throwable ->
                        LoadResult.Error(throwable)
                    }

                TvShow.TOP_RATED_TV_SHOWS -> apiService.getTopRatedTvShowsPaging(position)
                    .subscribeOn(Schedulers.io())
                    .map { data ->
                        toLoadResult(data, position)
                    }.onErrorReturn { throwable ->
                        LoadResult.Error(throwable)
                    }

                TvShow.POPULAR_TV_SHOWS -> apiService.getPopularTvShowsPaging(position)
                    .subscribeOn(Schedulers.io())
                    .map { data ->
                        toLoadResult(data, position)
                    }.onErrorReturn { throwable ->
                        LoadResult.Error(throwable)
                    }

                else -> apiService.getOnTheAirTvShowsPaging(position)
                    .subscribeOn(Schedulers.io())
                    .map { data ->
                        toLoadResult(data, position)
                    }.onErrorReturn { throwable ->
                        LoadResult.Error(throwable)
                    }
            }
        }
    }

    private fun toLoadResult(
        data: TvShowsResponse,
        position: Int
    ): LoadResult<Int, ListTvShowsResponse> {
        return LoadResult.Page(
            data = data.results ?: emptyList(),
            prevKey = if (position == 1) null else position - 1,
            nextKey = if (page != Page.ONE) {
                if (position == data.totalPages) null else position + 1
            } else {
                null
            }
        )
    }

    override fun getRefreshKey(state: PagingState<Int, ListTvShowsResponse>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    fun setData(tvShow: TvShow?, page: Page?, query: String?, tvId: Int?) {
        this.tvShow = tvShow
        this.page = page
        this.query = query
        this.tvId = tvId
    }

    companion object {
        private const val INITIAL_POSITION = 1
    }
}