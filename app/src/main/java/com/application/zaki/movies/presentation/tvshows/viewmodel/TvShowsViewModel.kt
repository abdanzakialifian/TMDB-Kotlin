package com.application.zaki.movies.presentation.tvshows.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.LiveDataReactiveStreams
import androidx.lifecycle.ViewModel
import androidx.paging.PagingData
import com.application.zaki.movies.domain.model.tvshows.*
import com.application.zaki.movies.domain.interfaces.ITvShowsUseCase
import com.application.zaki.movies.utils.UiState
import com.application.zaki.movies.utils.RxDisposer
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

    fun topRatedTvShows(rxDisposer: RxDisposer): LiveData<UiState<TopRatedTvShows>> {
        val subject = ReplaySubject.create<UiState<TopRatedTvShows>>()

        subject.onNext(UiState.Loading(null))
        tvShowsUseCase.getTopRatedTvShows()
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

    fun popularTvShows(rxDisposer: RxDisposer): LiveData<UiState<PopularTvShows>> {
        val subject = ReplaySubject.create<UiState<PopularTvShows>>()

        subject.onNext(UiState.Loading(null))
        tvShowsUseCase.getPopularTvShows()
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

    fun onTheAirTvShows(rxDisposer: RxDisposer): LiveData<UiState<OnTheAirTvShows>> {
        val subject = ReplaySubject.create<UiState<OnTheAirTvShows>>()

        subject.onNext(UiState.Loading(null))
        tvShowsUseCase.getOnTheAirTvShows()
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

    fun onTheAirTvShowsPaging(rxDisposer: RxDisposer): LiveData<UiState<PagingData<ListOnTheAirTvShows>>> {
        val subject = ReplaySubject.create<UiState<PagingData<ListOnTheAirTvShows>>>()

        subject.onNext(UiState.Loading(null))
        tvShowsUseCase.getOnTheAirTvShowsPaging()
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

    fun popularTvShowsPaging(rxDisposer: RxDisposer): LiveData<UiState<PagingData<ListPopularTvShows>>> {
        val subject = ReplaySubject.create<UiState<PagingData<ListPopularTvShows>>>()

        subject.onNext(UiState.Loading(null))
        tvShowsUseCase.getPopularTvShowsPaging()
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

    fun topRatedTvShowsPaging(rxDisposer: RxDisposer): LiveData<UiState<PagingData<ListTopRatedTvShows>>> {
        val subject = ReplaySubject.create<UiState<PagingData<ListTopRatedTvShows>>>()

        subject.onNext(UiState.Loading(null))
        tvShowsUseCase.getTopRatedTvShowsPaging()
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
}