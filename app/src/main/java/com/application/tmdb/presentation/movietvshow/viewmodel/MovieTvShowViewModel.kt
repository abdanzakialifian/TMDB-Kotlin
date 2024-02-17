package com.application.tmdb.presentation.movietvshow.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.rxjava2.cachedIn
import com.application.tmdb.core.domain.model.MovieTvShowModel
import com.application.tmdb.core.domain.usecase.movietvshow.MovieTvShowWrapper
import com.application.tmdb.common.Movie
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

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class MovieTvShowViewModel @Inject constructor(private val movieTvShowWrapper: MovieTvShowWrapper) :
    ViewModel() {

    private val _listMoviesPaging: MutableLiveData<PagingData<MovieTvShowModel>> = MutableLiveData()
    val listMoviesPaging get() = _listMoviesPaging.toLiveData()

    private val _listTvShowsPaging: MutableLiveData<PagingData<MovieTvShowModel>> = MutableLiveData()
    val listTvShowsPaging get() = _listTvShowsPaging.toLiveData()

    fun getListMovies(
        movie: com.application.tmdb.common.Movie?,
        page: com.application.tmdb.common.Page?,
        query: String?,
        movieId: Int?,
        rxDisposer: com.application.tmdb.common.RxDisposer
    ) {
        movieTvShowWrapper.getListMovies(movie, page, query, movieId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .cachedIn(viewModelScope)
            .subscribe { data ->
                _listMoviesPaging.postValue(data)
            }
            .addToDisposer(rxDisposer)
    }

    fun getListTvShows(
        tvShow: com.application.tmdb.common.TvShow?,
        page: com.application.tmdb.common.Page?,
        query: String?,
        tvId: Int?,
        rxDisposer: com.application.tmdb.common.RxDisposer
    ) {
        movieTvShowWrapper.getListTvShows(tvShow, page, query, tvId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .cachedIn(viewModelScope)
            .subscribe { data ->
                _listTvShowsPaging.postValue(data)
            }
            .addToDisposer(rxDisposer)
    }
}