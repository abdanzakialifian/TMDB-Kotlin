package com.application.zaki.movies.presentation.castcrew.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.application.zaki.movies.domain.model.DetailCastModel
import com.application.zaki.movies.domain.usecase.GetDetailCast
import com.application.zaki.movies.utils.RxDisposer
import com.application.zaki.movies.utils.UiState
import com.application.zaki.movies.utils.addToDisposer
import com.application.zaki.movies.utils.toLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

@HiltViewModel
class DetailCastCrewViewModel @Inject constructor(private val getDetailCast: GetDetailCast) :
    ViewModel() {

    private val _detailDataState = MutableLiveData<UiState<DetailCastModel>>()
    val detailDataState get() = _detailDataState.toLiveData()

    fun detailCast(personId: Int, rxDisposer: RxDisposer) {
        _detailDataState.postValue(UiState.Loading(null))
        getDetailCast(personId)
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
}