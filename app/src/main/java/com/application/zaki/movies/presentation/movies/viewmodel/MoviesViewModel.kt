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
    fun nowPlayingMovies(rxDisposer: RxDisposer): LiveData<UiState<NowPlayingMovies>> {
        val subject = ReplaySubject.create<UiState<NowPlayingMovies>>()

        subject.onNext(UiState.Loading(null))
        moviesUseCase.getNowPlayingMovies().subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread()).subscribe({ data ->
                if (data != null) {
                    subject.onNext(UiState.Success(data))
                } else {
                    subject.onNext(UiState.Empty)
                }
            }, { throwable ->
                subject.onNext(UiState.Error(throwable.message.toString()))
            }).addToDisposer(rxDisposer)

        // convert flowable to livedata
        return LiveDataReactiveStreams.fromPublisher(subject.toFlowable(BackpressureStrategy.BUFFER))
    }

    fun popularMoviesPaging(rxDisposer: RxDisposer, type: String, totalPage: String): LiveData<PagingData<ListPopularMovies>> {
        val subject = ReplaySubject.create<PagingData<ListPopularMovies>>()

        moviesUseCase.getPopularMoviesPaging(type, totalPage).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread()).subscribe { data ->
                subject.onNext(data)
            }.addToDisposer(rxDisposer)

        // convert flowable to livedata
        return LiveDataReactiveStreams.fromPublisher(subject.toFlowable(BackpressureStrategy.BUFFER))
    }

    fun topRatedMoviesPaging(rxDisposer: RxDisposer, type: String, totalPage: String): LiveData<PagingData<ListTopRatedMovies>> {
        val subject = ReplaySubject.create<PagingData<ListTopRatedMovies>>()

        moviesUseCase.getTopRatedMoviesPaging(type, totalPage).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread()).subscribe { data ->
                subject.onNext(data)
            }.addToDisposer(rxDisposer)

        // convert flowable to livedata
        return LiveDataReactiveStreams.fromPublisher(subject.toFlowable(BackpressureStrategy.BUFFER))
    }

    fun upComingMoviesPaging(rxDisposer: RxDisposer, type: String, totalPage: String): LiveData<PagingData<ListUpComingMovies>> {
        val subject = ReplaySubject.create<PagingData<ListUpComingMovies>>()

        moviesUseCase.getUpComingMoviesPaging(type, totalPage).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread()).subscribe { data ->
                subject.onNext(data)
            }.addToDisposer(rxDisposer)

        // convert flowable to livedata
        return LiveDataReactiveStreams.fromPublisher(subject.toFlowable(BackpressureStrategy.BUFFER))
    }
}