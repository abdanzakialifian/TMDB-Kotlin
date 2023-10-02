package com.application.zaki.movies.presentation.detail.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.toLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.rxjava2.cachedIn
import com.application.zaki.movies.domain.model.Detail
import com.application.zaki.movies.domain.model.MovieTvShow
import com.application.zaki.movies.domain.model.ReviewItem
import com.application.zaki.movies.domain.usecase.detail.DetailWrapper
import com.application.zaki.movies.utils.Category
import com.application.zaki.movies.utils.Movie
import com.application.zaki.movies.utils.Page
import com.application.zaki.movies.utils.RxDisposer
import com.application.zaki.movies.utils.UiState
import com.application.zaki.movies.utils.addToDisposer
import com.application.zaki.movies.utils.toLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.BackpressureStrategy
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.ReplaySubject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class DetailViewModel @Inject constructor(private val detailWrapper: DetailWrapper) : ViewModel() {

    private val _detailDataState = MutableLiveData<UiState<Detail>>()
    val detailDataState get() = _detailDataState.toLiveData()

    private val _listMoviesPaging: MutableLiveData<PagingData<MovieTvShow>> = MutableLiveData()
    val listMoviesPaging get() = _listMoviesPaging.toLiveData()

    private val _detailData = MutableLiveData<Pair<String, Detail>>()
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
        rxDisposer: RxDisposer, id: String, page: Page, category: Category
    ): LiveData<PagingData<ReviewItem>> {
        val subject = ReplaySubject.create<PagingData<ReviewItem>>()

        detailWrapper.getReviews(id, page, category)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { data ->
                subject.onNext(data)
            }
            .addToDisposer(rxDisposer)

        // convert flowable to livedata
        return subject.toFlowable(BackpressureStrategy.BUFFER).toLiveData()
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
                _listMoviesPaging.postValue(data)
            }
            .addToDisposer(rxDisposer)
    }

    fun setDetailData(intentFrom: String, detail: Detail) {
        _detailData.postValue(Pair(intentFrom, detail))
    }
}