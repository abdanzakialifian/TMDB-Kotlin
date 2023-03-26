package com.application.zaki.movies.data.source.remote.paging.tvshows

import androidx.paging.PagingState
import androidx.paging.rxjava2.RxPagingSource
import com.application.zaki.movies.data.source.remote.ApiService
import com.application.zaki.movies.data.source.remote.response.tvshows.ListTvShowsResponse
import com.application.zaki.movies.data.source.remote.response.tvshows.TvShowsResponse
import com.application.zaki.movies.utils.Page
import com.application.zaki.movies.utils.TvShow
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class TvShowsRxPagingSource @Inject constructor(private val apiService: ApiService) :
    RxPagingSource<Int, ListTvShowsResponse>() {

    private lateinit var page: Page
    private lateinit var tvShow: TvShow

    override fun loadSingle(params: LoadParams<Int>): Single<LoadResult<Int, ListTvShowsResponse>> {
        val position = if (page == Page.ONE) {
            1
        } else {
            params.key ?: 1
        }

        return when (tvShow) {
            TvShow.AIRING_TODAY_TV_SHOWS -> apiService.getAiringTodayTvShows(position)
                .subscribeOn(Schedulers.io())
                .map {
                    toLoadResult(it, position)
                }
                .onErrorReturn {
                    LoadResult.Error(it)
                }
            TvShow.TOP_RATED_TV_SHOWS -> apiService.getTopRatedTvShowsPaging(position)
                .subscribeOn(Schedulers.io())
                .map {
                    toLoadResult(it, position)
                }
                .onErrorReturn {
                    LoadResult.Error(it)
                }
            TvShow.POPULAR_TV_SHOWS -> apiService.getPopularTvShowsPaging(position)
                .subscribeOn(Schedulers.io())
                .map {
                    toLoadResult(it, position)
                }
                .onErrorReturn {
                    LoadResult.Error(it)
                }
            TvShow.ON_THE_AIR_TV_SHOWS -> apiService.getOnTheAirTvShowsPaging(position)
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
        data: TvShowsResponse,
        position: Int
    ): LoadResult<Int, ListTvShowsResponse> {
        return LoadResult.Page(
            data = data.results ?: emptyList(),
            prevKey = if (position == 1) null else position - 1,
            nextKey = if (page != Page.ONE) if (position == data.totalPages) null else position + 1 else null
        )
    }

    override fun getRefreshKey(state: PagingState<Int, ListTvShowsResponse>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    fun setData(tvShow: TvShow, page: Page) {
        this.tvShow = tvShow
        this.page = page
    }
}