package com.application.zaki.movies.presentation.list.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.LiveDataReactiveStreams
import androidx.lifecycle.ViewModel
import androidx.paging.PagingData
import com.application.zaki.movies.domain.interfaces.IMoviesUseCase
import com.application.zaki.movies.domain.model.movies.ResultsItemDiscover
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
class DiscoverViewModel @Inject constructor(private val movieUseCase: IMoviesUseCase) :
    ViewModel() {
    fun getDiscoverMovie(
        rxDisposer: RxDisposer,
        genreId: String
    ): LiveData<UiState<PagingData<ResultsItemDiscover>>> {
        val subject = ReplaySubject.create<UiState<PagingData<ResultsItemDiscover>>>()

        subject.onNext(UiState.Loading(null))
        movieUseCase.getDiscoverMovies(genreId)
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