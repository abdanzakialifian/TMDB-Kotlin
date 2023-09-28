package com.application.zaki.movies.presentation.detail.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.toLiveData
import androidx.paging.PagingData
import com.application.zaki.movies.domain.model.Detail
import com.application.zaki.movies.domain.model.ReviewItem
import com.application.zaki.movies.domain.usecase.GetDetailMovie
import com.application.zaki.movies.domain.usecase.GetDetailTvShow
import com.application.zaki.movies.domain.usecase.GetReviews
import com.application.zaki.movies.utils.Category
import com.application.zaki.movies.utils.Page
import com.application.zaki.movies.utils.RxDisposer
import com.application.zaki.movies.utils.UiState
import com.application.zaki.movies.utils.addToDisposer
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.BackpressureStrategy
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.ReplaySubject
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val getDetailMovie: GetDetailMovie,
    private val getDetailTvShow: GetDetailTvShow,
    private val getReviews: GetReviews
) : ViewModel() {
    fun detailMovies(
        rxDisposer: RxDisposer,
        movieId: String,
    ): LiveData<UiState<Detail>> {
        val subject = ReplaySubject.create<UiState<Detail>>()

        subject.onNext(UiState.Loading(null))
        getDetailMovie(movieId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { data ->
                    if (data != null) {
                        subject.onNext(UiState.Success(data))
                    } else {
                        subject.onNext(UiState.Empty)
                    }
                },
                { throwable ->
                    subject.onNext(UiState.Error(throwable.message.toString()))
                }
            )
            .addToDisposer(rxDisposer)

        // convert flowable to livedata
        return subject.toFlowable(BackpressureStrategy.BUFFER).toLiveData()
    }

    fun detailTvShows(
        rxDisposer: RxDisposer,
        tvId: String,
    ): LiveData<UiState<Detail>> {
        val subject = ReplaySubject.create<UiState<Detail>>()

        subject.onNext(UiState.Loading(null))
        getDetailTvShow(tvId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { data ->
                    if (data != null) {
                        subject.onNext(UiState.Success(data))
                    } else {
                        subject.onNext(UiState.Empty)
                    }
                },
                { throwable ->
                    subject.onNext(UiState.Error(throwable.message.toString()))
                }
            )
            .addToDisposer(rxDisposer)

        // convert flowable to livedata
        return subject.toFlowable(BackpressureStrategy.BUFFER).toLiveData()
    }

    fun reviewsPaging(
        rxDisposer: RxDisposer,
        id: String,
        page: Page,
        category: Category
    ): LiveData<PagingData<ReviewItem>> {
        val subject = ReplaySubject.create<PagingData<ReviewItem>>()

        getReviews(id, page, category)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { data ->
                subject.onNext(data)
            }
            .addToDisposer(rxDisposer)

        // convert flowable to livedata
        return subject.toFlowable(BackpressureStrategy.BUFFER).toLiveData()
    }
}