package com.application.tmdb.setting.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.application.tmdb.common.utils.RxDisposer
import com.application.tmdb.common.utils.addToDisposer
import com.application.tmdb.common.utils.toLiveData
import com.application.tmdb.domain.usecase.setting.SettingWrapper
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

@HiltViewModel
class SettingViewModel @Inject constructor(private val settingWrapper: SettingWrapper) :
    ViewModel() {
    private val _tmdbTheme = MutableLiveData<Boolean>()
    val tmdbTheme get() = _tmdbTheme.toLiveData()

    fun saveTMDBTheme(isDarkMode: Boolean) {
        settingWrapper.saveTMDBTheme(isDarkMode)
    }

    fun getTMDBTheme(rxDisposer: RxDisposer) {
        settingWrapper.getTMDBTheme()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { data ->
                _tmdbTheme.postValue(data)
            }.addToDisposer(rxDisposer)
    }
}