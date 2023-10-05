package com.application.zaki.movies.presentation.movie.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.rxjava2.cachedIn
import com.application.zaki.movies.domain.model.MovieTvShowModel
import com.application.zaki.movies.domain.usecase.movie.MovieWrapper
import com.application.zaki.movies.utils.Movie
import com.application.zaki.movies.utils.Page
import com.application.zaki.movies.utils.RxDisposer
import com.application.zaki.movies.utils.addToDisposer
import com.application.zaki.movies.utils.toLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject

@HiltViewModel
class MovieViewModel @Inject constructor(private val movieWrapper: MovieWrapper) : ViewModel() {

    private val _listMoviesPaging = MutableLiveData<List<Pair<Movie, PagingData<MovieTvShowModel>>>>()
    val listMoviesPaging get() = _listMoviesPaging.toLiveData()

    private val _listSearchMoviesPaging = MutableLiveData<PagingData<MovieTvShowModel>>()
    val listSearchMoviesPaging get() = _listSearchMoviesPaging.toLiveData()

    private val _isSearchStateChanged = MutableLiveData(false)
    val isSearchStateChanged get() = _isSearchStateChanged.toLiveData()

    fun getListAllMovies(
        nowPlayingMovie: Movie?,
        topRatedMovie: Movie?,
        popularMovie: Movie?,
        upComingMovie: Movie?,
        page: Page?,
        query: String?,
        movieId: Int?,
        rxDisposer: RxDisposer
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
        movie: Movie?,
        page: Page?,
        query: String?,
        movieId: Int?,
        rxDisposer: RxDisposer
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