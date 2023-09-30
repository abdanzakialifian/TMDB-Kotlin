package com.application.zaki.movies.presentation.tvshow.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.rxjava2.cachedIn
import com.application.zaki.movies.domain.model.MovieTvShow
import com.application.zaki.movies.domain.usecase.tvshow.TvShowWrapper
import com.application.zaki.movies.utils.Page
import com.application.zaki.movies.utils.RxDisposer
import com.application.zaki.movies.utils.TvShow
import com.application.zaki.movies.utils.addToDisposer
import com.application.zaki.movies.utils.toLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject

@HiltViewModel
class TvShowViewModel @Inject constructor(private val tvShowWrapper: TvShowWrapper) :
    ViewModel() {

    private val _listTvShows: MutableLiveData<List<Pair<TvShow, PagingData<MovieTvShow>>>> =
        MutableLiveData()
    val listTvShows get() = _listTvShows.toLiveData()

    private val _listSearchTvShows: MutableLiveData<PagingData<MovieTvShow>> =
        MutableLiveData()
    val listSearchTvShows get() = _listSearchTvShows.toLiveData()

    private val _isSearchStateChanged = MutableLiveData(false)
    val isSearchStateChanged get() = _isSearchStateChanged.toLiveData()

    fun getListAllTvShows(
        airingTodayTvShow: TvShow?,
        topRatedTvShow: TvShow?,
        popularTvShow: TvShow?,
        onTheAirTvShow: TvShow?,
        page: Page?,
        query: String?,
        rxDisposer: RxDisposer
    ) {
        tvShowWrapper.getListAllTvShows(
            airingTodayTvShow,
            topRatedTvShow,
            popularTvShow,
            onTheAirTvShow,
            page,
            query,
            viewModelScope
        )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { data ->
                _listTvShows.postValue(data)
            }.addToDisposer(rxDisposer)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    fun getListTvShows(
        tvShow: TvShow?,
        page: Page?,
        query: String?,
        rxDisposer: RxDisposer
    ) {
        tvShowWrapper.getListTvShows(tvShow, page, query)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .cachedIn(viewModelScope)
            .subscribe { data ->
                _listSearchTvShows.postValue(data)
            }
            .addToDisposer(rxDisposer)
    }

    fun setIsSearchStateChanged(isSearchStateChanged: Boolean) {
        this._isSearchStateChanged.postValue(isSearchStateChanged)
    }
}