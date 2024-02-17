package com.application.tmdb.presentation.tvshow.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.rxjava2.cachedIn
import com.application.tmdb.domain.model.MovieTvShowModel
import com.application.tmdb.domain.usecase.tvshow.TvShowWrapper
import com.application.tmdb.utils.Page
import com.application.tmdb.utils.RxDisposer
import com.application.tmdb.utils.TvShow
import com.application.tmdb.utils.addToDisposer
import com.application.tmdb.utils.toLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject

@HiltViewModel
class TvShowViewModel @Inject constructor(private val tvShowWrapper: TvShowWrapper) :
    ViewModel() {

    private val _listTvShowsPaging: MutableLiveData<List<Pair<TvShow, PagingData<MovieTvShowModel>>>> =
        MutableLiveData()
    val listTvShowsPaging get() = _listTvShowsPaging.toLiveData()

    private val _listSearchTvShowsPaging: MutableLiveData<PagingData<MovieTvShowModel>> =
        MutableLiveData()
    val listSearchTvShowsPaging get() = _listSearchTvShowsPaging.toLiveData()

    private val _isSearchStateChanged = MutableLiveData(false)
    val isSearchStateChanged get() = _isSearchStateChanged.toLiveData()

    fun getListAllTvShows(
        airingTodayTvShow: TvShow?,
        topRatedTvShow: TvShow?,
        popularTvShow: TvShow?,
        onTheAirTvShow: TvShow?,
        page: Page?,
        query: String?,
        tvId: Int?,
        rxDisposer: RxDisposer
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
        tvShow: TvShow?,
        page: Page?,
        query: String?,
        tvId: Int?,
        rxDisposer: RxDisposer
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