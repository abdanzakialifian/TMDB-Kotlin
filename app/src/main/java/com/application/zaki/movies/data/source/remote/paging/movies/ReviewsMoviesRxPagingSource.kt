package com.application.zaki.movies.data.source.remote.paging.movies

import androidx.paging.PagingState
import androidx.paging.rxjava2.RxPagingSource
import com.application.zaki.movies.data.source.remote.ApiService
import com.application.zaki.movies.data.source.remote.response.movies.ReviewItemResponse
import com.application.zaki.movies.data.source.remote.response.movies.ReviewsMovieResponse
import io.reactivex.Scheduler
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ReviewsMoviesRxPagingSource @Inject constructor(private val apiService: ApiService) :
    RxPagingSource<Int, ReviewItemResponse>() {
    private var movieId: String = ""

    override fun loadSingle(params: LoadParams<Int>): Single<LoadResult<Int, ReviewItemResponse>> {
        val position = params.key ?: 1

        return apiService.getReviewsMovie(movieId, position)
            .subscribeOn(Schedulers.io())
            .map {
                toLoadResult(it, position)
            }
            .onErrorReturn {
                LoadResult.Error(it)
            }
    }

    private fun toLoadResult(
        data: ReviewsMovieResponse,
        position: Int
    ): LoadResult<Int, ReviewItemResponse> {
        // mapping list because list is nullable
        val listReviewsMoviesResponse = ArrayList<ReviewItemResponse>()
        data.results?.map { map ->
            map?.let {
                listReviewsMoviesResponse.add(it)
            }
        }

        return LoadResult.Page(
            data = listReviewsMoviesResponse,
            prevKey = if (position == 1) null else position - 1,
            nextKey = if (position == data.totalPages) null else position + 1
        )
    }

    override fun getRefreshKey(state: PagingState<Int, ReviewItemResponse>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    fun getMovieId(movieId: String) {
        this.movieId = movieId
    }
}