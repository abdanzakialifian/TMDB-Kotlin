package com.application.zaki.movies.presentation.tvshows.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.LiveDataReactiveStreams
import androidx.lifecycle.ViewModel
import androidx.paging.PagingData
import com.application.zaki.movies.domain.interfaces.ITvShowsUseCase
import com.application.zaki.movies.domain.model.tvshows.*
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
class TvShowsViewModel @Inject constructor(private val tvShowsUseCase: ITvShowsUseCase) :
    ViewModel() {
    fun airingTodayTvShows(rxDisposer: RxDisposer): LiveData<UiState<AiringTodayTvShows>> {
        val subject = ReplaySubject.create<UiState<AiringTodayTvShows>>()

        subject.onNext(UiState.Loading(null))
        tvShowsUseCase.getAiringTodayTvShows()
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

    fun onTheAirTvShowsPaging(
        rxDisposer: RxDisposer,
        type: String,
        totalPage: String
    ): LiveData<PagingData<ListOnTheAirTvShows>> {
        val subject = ReplaySubject.create<PagingData<ListOnTheAirTvShows>>()

        tvShowsUseCase.getOnTheAirTvShowsPaging(type, totalPage)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { data ->
                subject.onNext(data)
            }
            .addToDisposer(rxDisposer)

        // convert flowable to livedata
        return LiveDataReactiveStreams.fromPublisher(subject.toFlowable(BackpressureStrategy.BUFFER))
    }

    fun popularTvShowsPaging(
        rxDisposer: RxDisposer,
        type: String,
        totalPage: String
    ): LiveData<PagingData<ListPopularTvShows>> {
        val subject = ReplaySubject.create<PagingData<ListPopularTvShows>>()

        tvShowsUseCase.getPopularTvShowsPaging(type, totalPage)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { data ->
                subject.onNext(data)
            }
            .addToDisposer(rxDisposer)

        // convert flowable to livedata
        return LiveDataReactiveStreams.fromPublisher(subject.toFlowable(BackpressureStrategy.BUFFER))
    }

    fun topRatedTvShowsPaging(
        rxDisposer: RxDisposer,
        type: String,
        totalPage: String
    ): LiveData<PagingData<ListTopRatedTvShows>> {
        val subject = ReplaySubject.create<PagingData<ListTopRatedTvShows>>()

        tvShowsUseCase.getTopRatedTvShowsPaging(type, totalPage)
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