package com.application.zaki.movies.presentation.list.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.LiveDataReactiveStreams
import androidx.lifecycle.ViewModel
import androidx.paging.PagingData
import com.application.zaki.movies.domain.interfaces.IMoviesUseCase
import com.application.zaki.movies.domain.model.movies.ResultsItemDiscover
import com.application.zaki.movies.utils.NetworkResult
import com.application.zaki.movies.utils.RxDisposer
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DiscoverViewModel @Inject constructor(private val movieUseCase: IMoviesUseCase) :
    ViewModel() {
    fun getDiscoverMovie(
        rxDisposer: RxDisposer,
        genreId: String
    ): LiveData<NetworkResult<PagingData<ResultsItemDiscover>>> =
        LiveDataReactiveStreams.fromPublisher(movieUseCase.getDiscoverMovies(rxDisposer, genreId))
}