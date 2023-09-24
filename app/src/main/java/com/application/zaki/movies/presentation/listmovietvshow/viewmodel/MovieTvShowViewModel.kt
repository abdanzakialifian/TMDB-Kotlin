package com.application.zaki.movies.presentation.listmovietvshow.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.application.zaki.movies.domain.model.MovieTvShow
import com.application.zaki.movies.domain.usecase.movietvshow.MovieTvShowWrapper
import com.application.zaki.movies.utils.Category
import com.application.zaki.movies.utils.Movie
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
class MovieTvShowViewModel @Inject constructor(private val movieTvShowWrapper: MovieTvShowWrapper) :
    ViewModel() {

    private val _listMovies: MutableLiveData<PagingData<MovieTvShow>> = MutableLiveData()
    val listMovies get() = _listMovies.toLiveData()

    private val _listTvShows: MutableLiveData<PagingData<MovieTvShow>> = MutableLiveData()
    val listTvShows get() = _listTvShows.toLiveData()

    fun getListMovies(
        movie: Movie,
        category: Category,
        page: Page,
        rxDisposer: RxDisposer
    ) {
        movieTvShowWrapper.getListMovies(movie, category, page, viewModelScope)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { data ->
                _listMovies.postValue(data)
            }
            .addToDisposer(rxDisposer)
    }

    fun getListTvShows(
        tvShow: TvShow,
        category: Category,
        page: Page,
        rxDisposer: RxDisposer
    ) {
        movieTvShowWrapper.getListTvShows(tvShow, category, page, viewModelScope)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { data ->
                _listTvShows.postValue(data)
            }
            .addToDisposer(rxDisposer)
    }
}