package com.application.zaki.movies.presentation.tvshows.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.LiveDataReactiveStreams
import androidx.lifecycle.ViewModel
import androidx.paging.PagingData
import com.application.zaki.movies.domain.model.MovieTvShow
import com.application.zaki.movies.domain.usecase.GetListAllTvShows
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
class TvShowsViewModel @Inject constructor(private val getListAllTvShows: GetListAllTvShows) :
    ViewModel() {

    fun getListAllTvShows(
        airingTodayTvShow: TvShow,
        topRatedTvShow: TvShow,
        popularTvShow: TvShow,
        onTheAirTvShow: TvShow,
        category: Category,
        page: Page,
        rxDisposer: RxDisposer
    ): LiveData<List<Pair<TvShow, PagingData<MovieTvShow>>>> {
        val subject = ReplaySubject.create<List<Pair<TvShow, PagingData<MovieTvShow>>>>()

        getListAllTvShows(
            airingTodayTvShow,
            topRatedTvShow,
            popularTvShow,
            onTheAirTvShow,
            category,
            page
        )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread()).subscribe { data ->
                subject.onNext(data)
            }.addToDisposer(rxDisposer)

        return LiveDataReactiveStreams.fromPublisher(subject.toFlowable(BackpressureStrategy.BUFFER))
    }
}