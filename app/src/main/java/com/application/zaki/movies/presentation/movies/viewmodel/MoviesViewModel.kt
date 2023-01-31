package com.application.zaki.movies.presentation.movies.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.LiveDataReactiveStreams
import androidx.lifecycle.ViewModel
import androidx.paging.PagingData
import com.application.zaki.movies.domain.interfaces.IMoviesUseCase
import com.application.zaki.movies.domain.model.movies.*
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
class MoviesViewModel @Inject constructor(private val moviesUseCase: IMoviesUseCase) : ViewModel() {
    fun popularMovies(rxDisposer: RxDisposer): LiveData<UiState<PopularMovies>> {
        // using replay subject so that loading is included emit
        val subject = ReplaySubject.create<UiState<PopularMovies>>()

        subject.onNext(UiState.Loading(null))
        moviesUseCase.getPopularMovies()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { data ->
                    if (data != null) {
                        subject.onNext(UiState.Success(data))
                    } else {
                        subject.onNext(UiState.Empty)
                    }
                }, { throwable ->
                    subject.onNext(UiState.Error(throwable.message.toString()))
                }
            )
            .addToDisposer(rxDisposer)

        // convert flowable to livedata
        return LiveDataReactiveStreams.fromPublisher(subject.toFlowable(BackpressureStrategy.BUFFER))
    }

    fun nowPlayingMovies(rxDisposer: RxDisposer): LiveData<UiState<NowPlayingMovies>> {
        val subject = ReplaySubject.create<UiState<NowPlayingMovies>>()

        subject.onNext(UiState.Loading(null))
        moviesUseCase.getNowPlayingMovies()
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

    fun topRatedMovies(rxDisposer: RxDisposer): LiveData<UiState<TopRatedMovies>> {
        val subject = ReplaySubject.create<UiState<TopRatedMovies>>()

        subject.onNext(UiState.Loading(null))
        moviesUseCase.getTopRatedMovies()
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

    fun upComingMovies(rxDisposer: RxDisposer): LiveData<UiState<UpComingMovies>> {
        val subject = ReplaySubject.create<UiState<UpComingMovies>>()

        subject.onNext(UiState.Loading(null))
        moviesUseCase.getUpComingMovies()
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

    fun popularMoviesPaging(rxDisposer: RxDisposer): LiveData<UiState<PagingData<ListPopularMovies>>> {
        val subject = ReplaySubject.create<UiState<PagingData<ListPopularMovies>>>()

        subject.onNext(UiState.Loading(null))
        moviesUseCase.getPopularMoviesPaging()
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

    fun topRatedMoviesPaging(rxDisposer: RxDisposer): LiveData<UiState<PagingData<ListTopRatedMovies>>> {
        val subject = ReplaySubject.create<UiState<PagingData<ListTopRatedMovies>>>()

        subject.onNext(UiState.Loading(null))
        moviesUseCase.getTopRatedMoviesPaging()
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

    fun upComingMoviesPaging(rxDisposer: RxDisposer): LiveData<UiState<PagingData<ListUpComingMovies>>> {
        val subject = ReplaySubject.create<UiState<PagingData<ListUpComingMovies>>>()

        subject.onNext(UiState.Loading(null))
        moviesUseCase.getUpComingMoviesPaging()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { value ->
                    if (value != null) {
                        subject.onNext(UiState.Success(value))
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