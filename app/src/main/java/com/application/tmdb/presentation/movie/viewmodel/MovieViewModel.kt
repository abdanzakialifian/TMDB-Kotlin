package com.application.tmdb.presentation.movie.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.rxjava2.cachedIn
import com.application.tmdb.core.domain.model.MovieTvShowModel
import com.application.tmdb.core.domain.usecase.movie.MovieWrapper
import com.application.tmdb.common.Movie
import com.application.tmdb.common.Page
import com.application.tmdb.common.RxDisposer
import com.application.tmdb.common.addToDisposer
import com.application.tmdb.common.toLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject

@HiltViewModel
class MovieViewModel @Inject constructor(private val movieWrapper: MovieWrapper) : ViewModel() {

    private val _listMoviesPaging = MutableLiveData<List<Pair<com.application.tmdb.common.Movie, PagingData<MovieTvShowModel>>>>()
    val listMoviesPaging get() = _listMoviesPaging.toLiveData()

    private val _listSearchMoviesPaging = MutableLiveData<PagingData<MovieTvShowModel>>()
    val listSearchMoviesPaging get() = _listSearchMoviesPaging.toLiveData()

    private val _isSearchStateChanged = MutableLiveData(false)
    val isSearchStateChanged get() = _isSearchStateChanged.toLiveData()

    fun getListAllMovies(
        nowPlayingMovie: com.application.tmdb.common.Movie?,
        topRatedMovie: com.application.tmdb.common.Movie?,
        popularMovie: com.application.tmdb.common.Movie?,
        upComingMovie: com.application.tmdb.common.Movie?,
        page: com.application.tmdb.common.Page?,
        query: String?,
        movieId: Int?,
        rxDisposer: com.application.tmdb.common.RxDisposer
    ) {
        movieWrapper.getListAllMovies(
            nowPlayingMovie,
            topRatedMovie,
            popularMovie,
            upComingMovie,
            page,
            query,
            movieId,
            viewModelScope
        )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { data ->
                _listMoviesPaging.postValue(data)

            }.addToDisposer(rxDisposer)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    fun getListMovies(
        movie: com.application.tmdb.common.Movie?,
        page: com.application.tmdb.common.Page?,
        query: String?,
        movieId: Int?,
        rxDisposer: com.application.tmdb.common.RxDisposer
    ) {
        movieWrapper.getListMovies(movie, page, query, movieId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .cachedIn(viewModelScope)
            .subscribe { data ->
                _listSearchMoviesPaging.postValue(data)
            }
            .addToDisposer(rxDisposer)
    }

    fun setIsSearchStateChanged(isSearchStateChanged: Boolean) {
        this._isSearchStateChanged.postValue(isSearchStateChanged)
    }
}