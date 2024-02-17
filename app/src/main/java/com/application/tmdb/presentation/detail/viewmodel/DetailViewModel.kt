package com.application.tmdb.presentation.detail.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.rxjava2.cachedIn
import com.application.tmdb.core.domain.model.DetailModel
import com.application.tmdb.core.domain.model.MovieTvShowModel
import com.application.tmdb.core.domain.model.ReviewModel
import com.application.tmdb.core.domain.usecase.detail.DetailWrapper
import com.application.tmdb.common.Movie
import com.application.tmdb.common.Page
import com.application.tmdb.common.RxDisposer
import com.application.tmdb.common.TvShow
import com.application.tmdb.common.UiState
import com.application.tmdb.common.addToDisposer
import com.application.tmdb.common.toLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class DetailViewModel @Inject constructor(private val detailWrapper: DetailWrapper) : ViewModel() {

    private val _detailDataState = MutableLiveData<com.application.tmdb.common.UiState<DetailModel>>()
    val detailDataState get() = _detailDataState.toLiveData()

    private val _listSimilarPaging: MutableLiveData<PagingData<MovieTvShowModel>> = MutableLiveData()
    val listSimilarPaging get() = _listSimilarPaging.toLiveData()

    private val _listReviewsPaging: MutableLiveData<PagingData<ReviewModel>> = MutableLiveData()
    val listReviewsPaging get() = _listReviewsPaging.toLiveData()

    private val _detailData = MutableLiveData<Pair<String, DetailModel>>()
    val detailData get() = _detailData.toLiveData()

    fun detailMovies(
        movieId: String,
        rxDisposer: com.application.tmdb.common.RxDisposer,
    ) {
        _detailDataState.postValue(com.application.tmdb.common.UiState.Loading(null))
        detailWrapper.getDetailMovie(movieId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ data ->
                if (data != null) {
                    _detailDataState.postValue(com.application.tmdb.common.UiState.Success(data))
                } else {
                    _detailDataState.postValue(com.application.tmdb.common.UiState.Empty)
                }
            }, { throwable ->
                _detailDataState.postValue(com.application.tmdb.common.UiState.Error(throwable.message.toString()))
            })
            .addToDisposer(rxDisposer)
    }

    fun detailTvShows(
        tvId: String,
        rxDisposer: com.application.tmdb.common.RxDisposer,
    ) {
        _detailDataState.postValue(com.application.tmdb.common.UiState.Loading(null))
        detailWrapper.getDetailTvShow(tvId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ data ->
                if (data != null) {
                    _detailDataState.postValue(com.application.tmdb.common.UiState.Success(data))
                } else {
                    _detailDataState.postValue(com.application.tmdb.common.UiState.Empty)
                }
            }, { throwable ->
                _detailDataState.postValue(com.application.tmdb.common.UiState.Error(throwable.message.toString()))
            })
            .addToDisposer(rxDisposer)
    }

    fun reviewsPaging(
        id: String?,
        category: com.application.tmdb.common.Category?,
        rxDisposer: com.application.tmdb.common.RxDisposer,
    ) {
        detailWrapper.getReviews(id, category)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { data ->
                _listReviewsPaging.postValue(data)
            }
            .addToDisposer(rxDisposer)
    }

    fun getSimilarMovies(
        movie: com.application.tmdb.common.Movie?,
        page: com.application.tmdb.common.Page?,
        query: String?,
        movieId: Int?,
        rxDisposer: com.application.tmdb.common.RxDisposer
    ) {
        detailWrapper.getListMovies(movie, page, query, movieId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .cachedIn(viewModelScope)
            .subscribe { data ->
                _listSimilarPaging.postValue(data)
            }
            .addToDisposer(rxDisposer)
    }

    fun getSimilarTvShows(
        tvShow: com.application.tmdb.common.TvShow?,
        page: com.application.tmdb.common.Page?,
        query: String?,
        tvId: Int?,
        rxDisposer: com.application.tmdb.common.RxDisposer
    ) {
        detailWrapper.getListTvShows(tvShow, page, query, tvId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .cachedIn(viewModelScope)
            .subscribe { data ->
                _listSimilarPaging.postValue(data)
            }
            .addToDisposer(rxDisposer)
    }

    fun setDetailData(intentFrom: String, detail: DetailModel) {
        _detailData.postValue(Pair(intentFrom, detail))
    }
}