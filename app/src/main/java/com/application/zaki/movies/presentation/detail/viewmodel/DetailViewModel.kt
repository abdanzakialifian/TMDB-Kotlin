package com.application.zaki.movies.presentation.detail.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.rxjava2.cachedIn
import com.application.zaki.movies.domain.model.DetailModel
import com.application.zaki.movies.domain.model.MovieTvShowModel
import com.application.zaki.movies.domain.model.ReviewModel
import com.application.zaki.movies.domain.usecase.detail.DetailWrapper
import com.application.zaki.movies.utils.Category
import com.application.zaki.movies.utils.Movie
import com.application.zaki.movies.utils.Page
import com.application.zaki.movies.utils.RxDisposer
import com.application.zaki.movies.utils.TvShow
import com.application.zaki.movies.utils.UiState
import com.application.zaki.movies.utils.addToDisposer
import com.application.zaki.movies.utils.toLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class DetailViewModel @Inject constructor(private val detailWrapper: DetailWrapper) : ViewModel() {

    private val _detailDataState = MutableLiveData<UiState<DetailModel>>()
    val detailDataState get() = _detailDataState.toLiveData()

    private val _listSimilarPaging: MutableLiveData<PagingData<MovieTvShowModel>> = MutableLiveData()
    val listSimilarPaging get() = _listSimilarPaging.toLiveData()

    private val _listReviewsPaging: MutableLiveData<PagingData<ReviewModel>> = MutableLiveData()
    val listReviewsPaging get() = _listReviewsPaging.toLiveData()

    private val _detailData = MutableLiveData<Pair<String, DetailModel>>()
    val detailData get() = _detailData.toLiveData()

    fun detailMovies(
        movieId: String,
        rxDisposer: RxDisposer,
    ) {
        _detailDataState.postValue(UiState.Loading(null))
        detailWrapper.getDetailMovie(movieId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ data ->
                if (data != null) {
                    _detailDataState.postValue(UiState.Success(data))
                } else {
                    _detailDataState.postValue(UiState.Empty)
                }
            }, { throwable ->
                _detailDataState.postValue(UiState.Error(throwable.message.toString()))
            })
            .addToDisposer(rxDisposer)
    }

    fun detailTvShows(
        tvId: String,
        rxDisposer: RxDisposer,
    ) {
        _detailDataState.postValue(UiState.Loading(null))
        detailWrapper.getDetailTvShow(tvId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ data ->
                if (data != null) {
                    _detailDataState.postValue(UiState.Success(data))
                } else {
                    _detailDataState.postValue(UiState.Empty)
                }
            }, { throwable ->
                _detailDataState.postValue(UiState.Error(throwable.message.toString()))
            })
            .addToDisposer(rxDisposer)
    }

    fun reviewsPaging(
        id: String?,
        category: Category?,
        rxDisposer: RxDisposer,
    ) {
        detailWrapper.getReviews(id, category)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .cachedIn(viewModelScope)
            .subscribe { data ->
                _listReviewsPaging.postValue(data)
            }
            .addToDisposer(rxDisposer)
    }

    fun getSimilarMovies(
        movie: Movie?,
        page: Page?,
        query: String?,
        movieId: Int?,
        rxDisposer: RxDisposer
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
        tvShow: TvShow?,
        page: Page?,
        query: String?,
        tvId: Int?,
        rxDisposer: RxDisposer
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