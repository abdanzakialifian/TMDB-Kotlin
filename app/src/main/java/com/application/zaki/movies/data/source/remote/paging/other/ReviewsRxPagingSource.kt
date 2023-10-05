package com.application.zaki.movies.data.source.remote.paging.other

import androidx.paging.PagingState
import androidx.paging.rxjava2.RxPagingSource
import com.application.zaki.movies.data.source.remote.ApiService
import com.application.zaki.movies.data.source.remote.response.other.ReviewItemResponse
import com.application.zaki.movies.data.source.remote.response.other.ReviewsResponse
import com.application.zaki.movies.utils.Category
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ReviewsRxPagingSource @Inject constructor(private val apiService: ApiService) :
    RxPagingSource<Int, ReviewItemResponse>() {

    private var id: String? = null

    private var category: Category? = null

    override fun loadSingle(params: LoadParams<Int>): Single<LoadResult<Int, ReviewItemResponse>> {
        val position = params.key ?: INITIAL_POSITION

        return if (category == Category.MOVIES) {
            apiService.getReviewsMovie(id ?: "", position)
                .subscribeOn(Schedulers.io())
                .map { data ->
                    toLoadResult(data, position)
                }
                .onErrorReturn { throwable ->
                    LoadResult.Error(throwable)
                }
        } else {
            apiService.getReviewsTvShow(id ?: "", position)
                .subscribeOn(Schedulers.io())
                .map { data ->
                    toLoadResult(data, position)
                }
                .onErrorReturn { throwable ->
                    LoadResult.Error(throwable)
                }
        }
    }

    private fun toLoadResult(
        data: ReviewsResponse,
        position: Int
    ): LoadResult<Int, ReviewItemResponse> {
        return LoadResult.Page(
            data = data.results ?: emptyList(),
            prevKey = if (position == 1) null else position - 1,
            nextKey = if (position == data.totalPages || data.totalPages == 0) null else position + 1
        )
    }

    override fun getRefreshKey(state: PagingState<Int, ReviewItemResponse>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    fun setDataReviews(id: String?, category: Category?) {
        this.id = id
        this.category = category
    }

    companion object {
        private const val INITIAL_POSITION = 1
    }
}