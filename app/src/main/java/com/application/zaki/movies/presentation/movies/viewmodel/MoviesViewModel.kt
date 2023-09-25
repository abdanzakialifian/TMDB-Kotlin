package com.application.zaki.movies.presentation.movies.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.application.zaki.movies.domain.model.MovieTvShow
import com.application.zaki.movies.domain.usecase.GetListAllMovies
import com.application.zaki.movies.utils.Movie
import com.application.zaki.movies.utils.Page
import com.application.zaki.movies.utils.RxDisposer
import com.application.zaki.movies.utils.addToDisposer
import com.application.zaki.movies.utils.toLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

@HiltViewModel
class MoviesViewModel @Inject constructor(private val getListAllMovies: GetListAllMovies) :
    ViewModel() {

    private val _listMovies = MutableLiveData<List<Pair<Movie, PagingData<MovieTvShow>>>>()
    val listMovies get() = _listMovies.toLiveData()

    fun getListAllMovies(
        nowPlayingMovie: Movie,
        topRatedMovie: Movie,
        popularMovie: Movie,
        upComingMovie: Movie,
        page: Page,
        rxDisposer: RxDisposer
    ) {
        getListAllMovies(
            nowPlayingMovie,
            topRatedMovie,
            popularMovie,
            upComingMovie,
            page,
            viewModelScope
        )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { data ->
                _listMovies.postValue(data)

            }.addToDisposer(rxDisposer)
    }
}