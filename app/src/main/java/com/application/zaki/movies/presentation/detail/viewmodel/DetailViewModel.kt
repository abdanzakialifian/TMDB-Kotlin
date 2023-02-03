package com.application.zaki.movies.presentation.detail.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.LiveDataReactiveStreams
import androidx.lifecycle.ViewModel
import androidx.paging.PagingData
import com.application.zaki.movies.domain.interfaces.ICombineUseCase
import com.application.zaki.movies.domain.interfaces.IMoviesUseCase
import com.application.zaki.movies.domain.interfaces.ITvShowsUseCase
import com.application.zaki.movies.domain.model.movies.DetailMovies
import com.application.zaki.movies.domain.model.movies.ReviewItem
import com.application.zaki.movies.domain.model.tvshows.DetailTvShows
import com.application.zaki.movies.utils.*
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.BackpressureStrategy
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.ReplaySubject
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val movieUseCase: IMoviesUseCase,
    private val tvShowsUseCase: ITvShowsUseCase,
    private val combineUseCase: ICombineUseCase
) : ViewModel() {
    fun detailMovies(
        rxDisposer: RxDisposer,
        movieId: String,
    ): LiveData<UiState<DetailMovies>> {
        val subject = ReplaySubject.create<UiState<DetailMovies>>()

        subject.onNext(UiState.Loading(null))
        movieUseCase.getDetailMovies(movieId)
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
        return LiveDataReactiveStreams.fromPublisher(subject.toFlowable(BackpressureStrategy.BUFFER))
    }

    fun detailTvShows(
        rxDisposer: RxDisposer,
        tvId: String,
    ): LiveData<UiState<DetailTvShows>> {
        val subject = ReplaySubject.create<UiState<DetailTvShows>>()

        subject.onNext(UiState.Loading(null))
        tvShowsUseCase.getDetailTvShows(tvId)
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
        return LiveDataReactiveStreams.fromPublisher(subject.toFlowable(BackpressureStrategy.BUFFER))
    }

    fun reviewsPaging(
        rxDisposer: RxDisposer,
        movieId: String,
        totalPage: String,
        type: String
    ): LiveData<PagingData<ReviewItem>> {
        val subject = ReplaySubject.create<PagingData<ReviewItem>>()

        combineUseCase.getReviewsPaging(movieId, totalPage, type)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { data ->
                subject.onNext(data)
            }
            .addToDisposer(rxDisposer)

        // convert flowable to livedata
        return LiveDataReactiveStreams.fromPublisher(subject.toFlowable(BackpressureStrategy.BUFFER))
    }
}