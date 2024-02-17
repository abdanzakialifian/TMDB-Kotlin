package com.application.tmdb.presentation.tvshow.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.rxjava2.cachedIn
import com.application.tmdb.core.domain.model.MovieTvShowModel
import com.application.tmdb.core.domain.usecase.tvshow.TvShowWrapper
import com.application.tmdb.common.Page
import com.application.tmdb.common.TvShow
import com.application.tmdb.common.RxDisposer
import com.application.tmdb.common.addToDisposer
import com.application.tmdb.common.toLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject

@HiltViewModel
class TvShowViewModel @Inject constructor(private val tvShowWrapper: TvShowWrapper) :
    ViewModel() {

    private val _listTvShowsPaging: MutableLiveData<List<Pair<com.application.tmdb.common.TvShow, PagingData<MovieTvShowModel>>>> =
        MutableLiveData()
    val listTvShowsPaging get() = _listTvShowsPaging.toLiveData()

    private val _listSearchTvShowsPaging: MutableLiveData<PagingData<MovieTvShowModel>> =
        MutableLiveData()
    val listSearchTvShowsPaging get() = _listSearchTvShowsPaging.toLiveData()

    private val _isSearchStateChanged = MutableLiveData(false)
    val isSearchStateChanged get() = _isSearchStateChanged.toLiveData()

    fun getListAllTvShows(
        airingTodayTvShow: com.application.tmdb.common.TvShow?,
        topRatedTvShow: com.application.tmdb.common.TvShow?,
        popularTvShow: com.application.tmdb.common.TvShow?,
        onTheAirTvShow: com.application.tmdb.common.TvShow?,
        page: com.application.tmdb.common.Page?,
        query: String?,
        tvId: Int?,
        rxDisposer: com.application.tmdb.common.RxDisposer
    ) {
        tvShowWrapper.getListAllTvShows(
            airingTodayTvShow,
            topRatedTvShow,
            popularTvShow,
            onTheAirTvShow,
            page,
            query,
            tvId,
            viewModelScope
        )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { data ->
                _listTvShowsPaging.postValue(data)
            }.addToDisposer(rxDisposer)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    fun getListTvShows(
        tvShow: com.application.tmdb.common.TvShow?,
        page: com.application.tmdb.common.Page?,
        query: String?,
        tvId: Int?,
        rxDisposer: com.application.tmdb.common.RxDisposer
    ) {
        tvShowWrapper.getListTvShows(tvShow, page, query, tvId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .cachedIn(viewModelScope)
            .subscribe { data ->
                _listSearchTvShowsPaging.postValue(data)
            }
            .addToDisposer(rxDisposer)
    }

    fun setIsSearchStateChanged(isSearchStateChanged: Boolean) {
        this._isSearchStateChanged.postValue(isSearchStateChanged)
    }
}