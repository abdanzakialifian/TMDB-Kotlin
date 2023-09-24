package com.application.zaki.movies.presentation.home.tvshows.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.application.zaki.movies.domain.model.MovieTvShow
import com.application.zaki.movies.domain.usecase.GetListAllTvShows
import com.application.zaki.movies.utils.Category
import com.application.zaki.movies.utils.Page
import com.application.zaki.movies.utils.RxDisposer
import com.application.zaki.movies.utils.TvShow
import com.application.zaki.movies.utils.addToDisposer
import com.application.zaki.movies.utils.toLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

@HiltViewModel
class TvShowsViewModel @Inject constructor(private val getListAllTvShows: GetListAllTvShows) :
    ViewModel() {

    private val _listTvShows: MutableLiveData<List<Pair<TvShow, PagingData<MovieTvShow>>>> =
        MutableLiveData()
    val listTvShows get() = _listTvShows.toLiveData()

    fun getListAllTvShows(
        airingTodayTvShow: TvShow,
        topRatedTvShow: TvShow,
        popularTvShow: TvShow,
        onTheAirTvShow: TvShow,
        category: Category,
        page: Page,
        rxDisposer: RxDisposer
    ) {
        getListAllTvShows(
            airingTodayTvShow,
            topRatedTvShow,
            popularTvShow,
            onTheAirTvShow,
            category,
            page,
            viewModelScope
        )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { data ->
                _listTvShows.postValue(data)
            }.addToDisposer(rxDisposer)
    }
}