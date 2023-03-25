package com.application.zaki.movies.presentation.movies.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.LiveDataReactiveStreams
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.application.zaki.movies.domain.interfaces.IMoviesUseCase
import com.application.zaki.movies.domain.model.movies.ListMovies
import com.application.zaki.movies.utils.Genre
import com.application.zaki.movies.utils.Movie
import com.application.zaki.movies.utils.Page
import com.application.zaki.movies.utils.RxDisposer
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MoviesViewModel @Inject constructor(private val moviesUseCase: IMoviesUseCase) : ViewModel() {
    fun getMovies(
        movie: Movie,
        genre: Genre,
        page: Page,
        rxDisposer: RxDisposer
    ): LiveData<PagingData<ListMovies>> = LiveDataReactiveStreams.fromPublisher(
        moviesUseCase.getMovies(
            movie,
            genre,
            page,
            rxDisposer
        )
    ).cachedIn(viewModelScope)
}