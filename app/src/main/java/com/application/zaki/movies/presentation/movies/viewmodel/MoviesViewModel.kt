package com.application.zaki.movies.presentation.movies.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.LiveDataReactiveStreams
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.application.zaki.movies.domain.model.movies.ListMovies
import com.application.zaki.movies.domain.usecase.GetListMovies
import com.application.zaki.movies.utils.Category
import com.application.zaki.movies.utils.Movie
import com.application.zaki.movies.utils.Page
import com.application.zaki.movies.utils.RxDisposer
import com.application.zaki.movies.utils.addToDisposer
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.BackpressureStrategy
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MoviesViewModel @Inject constructor(private val getListMovies: GetListMovies) : ViewModel() {
    fun getMovies(
        movie: Movie, category: Category, page: Page, rxDisposer: RxDisposer
    ): LiveData<PagingData<ListMovies>> {
        val subject = PublishSubject.create<PagingData<ListMovies>>()

        viewModelScope.launch {
            getListMovies(movie, category, page)
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