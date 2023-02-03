package com.application.zaki.movies.data.source.remote.paging.combine

import androidx.paging.PagingState
import androidx.paging.rxjava2.RxPagingSource
import com.application.zaki.movies.data.source.remote.ApiService
import com.application.zaki.movies.data.source.remote.response.combine.ReviewItemResponse
import com.application.zaki.movies.data.source.remote.response.combine.ReviewsResponse
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ReviewsRxPagingSource @Inject constructor(private val apiService: ApiService) :
    RxPagingSource<Int, ReviewItemResponse>() {

    private var id: String = ""
    private var totalPage: String = ""
    private var type: String = ""

    override fun loadSingle(params: LoadParams<Int>): Single<LoadResult<Int, ReviewItemResponse>> {
        val position: Int = if (totalPage == ONE) {
            1
        } else {
            params.key ?: 1
        }

        return if (type == MOVIES) {
            apiService.getReviewsMovie(id, position)
                .subscribeOn(Schedulers.io())
                .map {
                    toLoadResult(it, position)
                }
                .onErrorReturn {
                    LoadResult.Error(it)
                }
        } else {
            apiService.getReviewsTvShow(id, position)
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
        data: ReviewsResponse,
        position: Int
    ): LoadResult<Int, ReviewItemResponse> {
        // mapping list because list is nullable
        val listReviewsResponse = ArrayList<ReviewItemResponse>()
        data.results?.map { map ->
            map?.let {
                listReviewsResponse.add(it)
            }
        }

        return LoadResult.Page(
            data = listReviewsResponse,
            prevKey = if (position == 1) null else position - 1,
            nextKey = if (totalPage != ONE) if (position == data.totalPages) null else position + 1 else null
        )
    }

    override fun getRefreshKey(state: PagingState<Int, ReviewItemResponse>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    fun setDataReviews(id: String, totalPage: String, type: String) {
        this.id = id
        this.totalPage = totalPage
        this.type = type
    }

    companion object {
        const val ONE = "ONE"
        const val MORE_THAN_ONE = "MORE THAN ONE"
        const val MOVIES = "MOVIES"
        const val TV_SHOWS = "TV SHOWS"
    }
}