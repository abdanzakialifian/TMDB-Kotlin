package com.application.zaki.movies.presentation.movies.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.LiveDataReactiveStreams
import androidx.lifecycle.ViewModel
import androidx.paging.PagingData
import com.application.zaki.movies.domain.model.MovieTvShow
import com.application.zaki.movies.domain.usecase.GetListAllMovies
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
import javax.inject.Inject

@HiltViewModel
class MoviesViewModel @Inject constructor(private val getListAllMovies: GetListAllMovies) :
    ViewModel() {
    fun getListAllMovies(
        nowPlayingMovie: Movie,
        topRatedMovie: Movie,
        popularMovie: Movie,
        upComingMovie: Movie,
        category: Category,
        page: Page,
        rxDisposer: RxDisposer
    ): LiveData<List<Pair<Movie, PagingData<MovieTvShow>>>> {
        val subject = PublishSubject.create<List<Pair<Movie, PagingData<MovieTvShow>>>>()

        getListAllMovies(
            nowPlayingMovie,
            topRatedMovie,
            popularMovie,
            upComingMovie,
            category,
            page
        )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { data ->
                subject.onNext(data)

            }.addToDisposer(rxDisposer)

        return LiveDataReactiveStreams.fromPublisher(subject.toFlowable(BackpressureStrategy.BUFFER))
    }
}