package com.application.tmdb.presentation.castcrew.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.application.tmdb.core.domain.model.DetailCastModel
import com.application.tmdb.core.domain.usecase.GetDetailCast
import com.application.tmdb.common.RxDisposer
import com.application.tmdb.common.UiState
import com.application.tmdb.common.addToDisposer
import com.application.tmdb.common.toLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

@HiltViewModel
class DetailCastCrewViewModel @Inject constructor(private val getDetailCast: GetDetailCast) :
    ViewModel() {

    private val _detailDataState = MutableLiveData<com.application.tmdb.common.UiState<DetailCastModel>>()
    val detailDataState get() = _detailDataState.toLiveData()

    fun detailCast(personId: Int, rxDisposer: com.application.tmdb.common.RxDisposer) {
        _detailDataState.postValue(com.application.tmdb.common.UiState.Loading(null))
        getDetailCast(personId)
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
}