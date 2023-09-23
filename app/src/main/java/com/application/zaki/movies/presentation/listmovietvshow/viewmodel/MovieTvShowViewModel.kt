package com.application.zaki.movies.presentation.listmovietvshow.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.LiveDataReactiveStreams
import androidx.lifecycle.ViewModel
import androidx.paging.PagingData
import com.application.zaki.movies.domain.model.MovieTvShow
import com.application.zaki.movies.domain.usecase.movietvshow.MovieTvShowWrapper
import com.application.zaki.movies.utils.Category
import com.application.zaki.movies.utils.Movie
import com.application.zaki.movies.utils.Page
import com.application.zaki.movies.utils.RxDisposer
import com.application.zaki.movies.utils.TvShow
import com.application.zaki.movies.utils.addToDisposer
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.BackpressureStrategy
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject

@HiltViewModel
class MovieTvShowViewModel @Inject constructor(private val movieTvShowWrapper: MovieTvShowWrapper) :
    ViewModel() {
    fun getListMovies(
        movie: Movie,
        category: Category,
        page: Page,
        rxDisposer: RxDisposer
    ): LiveData<PagingData<MovieTvShow>> {
        val subject = PublishSubject.create<PagingData<MovieTvShow>>()

        movieTvShowWrapper.getListMovies(movie, category, page)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { data ->
                subject.onNext(data)
            }
            .addToDisposer(rxDisposer)

        return LiveDataReactiveStreams.fromPublisher(subject.toFlowable(BackpressureStrategy.BUFFER))
    }

    fun getListTvShows(
        tvShow: TvShow,
        category: Category,
        page: Page,
        rxDisposer: RxDisposer
    ): LiveData<PagingData<MovieTvShow>> {
        val subject = PublishSubject.create<PagingData<MovieTvShow>>()

        movieTvShowWrapper.getListTvShows(tvShow, category, page)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { data ->
                subject.onNext(data)
            }
            .addToDisposer(rxDisposer)

        return LiveDataReactiveStreams.fromPublisher(subject.toFlowable(BackpressureStrategy.BUFFER))
    }
}