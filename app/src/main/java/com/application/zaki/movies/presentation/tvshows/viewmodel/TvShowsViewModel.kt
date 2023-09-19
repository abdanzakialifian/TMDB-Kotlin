package com.application.zaki.movies.presentation.tvshows.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.LiveDataReactiveStreams
import androidx.lifecycle.ViewModel
import androidx.paging.PagingData
import com.application.zaki.movies.domain.model.tvshows.ListTvShows
import com.application.zaki.movies.domain.usecase.GetListTvShows
import com.application.zaki.movies.utils.Category
import com.application.zaki.movies.utils.Page
import com.application.zaki.movies.utils.RxDisposer
import com.application.zaki.movies.utils.TvShow
import com.application.zaki.movies.utils.addToDisposer
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.BackpressureStrategy
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.ReplaySubject
import javax.inject.Inject

@HiltViewModel
class TvShowsViewModel @Inject constructor(private val getListTvShows: GetListTvShows) :
    ViewModel() {
    fun airingTodayTvShows(
        rxDisposer: RxDisposer,
        tvShow: TvShow,
        category: Category,
        page: Page
    ): LiveData<PagingData<ListTvShows>> {
        val subject = ReplaySubject.create<PagingData<ListTvShows>>()
        getListTvShows(tvShow, category, page)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { data ->
                subject.onNext(data)
            }
            .addToDisposer(rxDisposer)

        // convert flowable to livedata
        return LiveDataReactiveStreams.fromPublisher(subject.toFlowable(BackpressureStrategy.BUFFER))
    }

    fun onTheAirTvShowsPaging(
        rxDisposer: RxDisposer, tvShow: TvShow, category: Category, page: Page
    ): LiveData<PagingData<ListTvShows>> {
        val subject = ReplaySubject.create<PagingData<ListTvShows>>()

        getListTvShows(tvShow, category, page)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { data ->
                subject.onNext(data)
            }.addToDisposer(rxDisposer)

        // convert flowable to livedata
        return LiveDataReactiveStreams.fromPublisher(subject.toFlowable(BackpressureStrategy.BUFFER))
    }

    fun popularTvShowsPaging(
        rxDisposer: RxDisposer,
        tvShow: TvShow,
        category: Category,
        page: Page
    ): LiveData<PagingData<ListTvShows>> {
        val subject = ReplaySubject.create<PagingData<ListTvShows>>()

        getListTvShows(tvShow, category, page)
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
        tvShow: TvShow,
        category: Category,
        page: Page
    ): LiveData<PagingData<ListTvShows>> {
        val subject = ReplaySubject.create<PagingData<ListTvShows>>()

        getListTvShows(tvShow, category, page)
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