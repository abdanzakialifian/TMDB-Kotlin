package com.application.zaki.movies.presentation.movies.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.LiveDataReactiveStreams
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.application.zaki.movies.domain.interfaces.IMoviesUseCase
import com.application.zaki.movies.domain.model.movies.ListMovies
import com.application.zaki.movies.utils.*
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.BackpressureStrategy
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MoviesViewModel @Inject constructor(private val moviesUseCase: IMoviesUseCase) : ViewModel() {
    fun getMovies(
        movie: Movie,
        genre: Genre,
        page: Page,
        rxDisposer: RxDisposer
    ): LiveData<PagingData<ListMovies>> {
        val subject = PublishSubject.create<PagingData<ListMovies>>()

        viewModelScope.launch {
            moviesUseCase.getMovies(movie, genre, page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { data ->
                    subject.onNext(data)
                }.addToDisposer(rxDisposer)
        }

        return LiveDataReactiveStreams.fromPublisher(subject.toFlowable(BackpressureStrategy.BUFFER))
            .cachedIn(viewModelScope)
    }
}