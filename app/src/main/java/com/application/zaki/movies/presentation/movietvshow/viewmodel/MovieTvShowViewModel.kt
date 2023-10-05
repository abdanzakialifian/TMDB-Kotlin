package com.application.zaki.movies.presentation.movietvshow.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.rxjava2.cachedIn
import com.application.zaki.movies.domain.model.MovieTvShowModel
import com.application.zaki.movies.domain.usecase.movietvshow.MovieTvShowWrapper
import com.application.zaki.movies.utils.Movie
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

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class MovieTvShowViewModel @Inject constructor(private val movieTvShowWrapper: MovieTvShowWrapper) :
    ViewModel() {

    private val _listMoviesPaging: MutableLiveData<PagingData<MovieTvShowModel>> = MutableLiveData()
    val listMoviesPaging get() = _listMoviesPaging.toLiveData()

    private val _listTvShowsPaging: MutableLiveData<PagingData<MovieTvShowModel>> = MutableLiveData()
    val listTvShowsPaging get() = _listTvShowsPaging.toLiveData()

    fun getListMovies(
        movie: Movie?,
        page: Page?,
        query: String?,
        movieId: Int?,
        rxDisposer: RxDisposer
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
        tvShow: TvShow?,
        page: Page?,
        query: String?,
        tvId: Int?,
        rxDisposer: RxDisposer
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