package com.application.zaki.movies.presentation.list.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.LiveDataReactiveStreams
import androidx.lifecycle.ViewModel
import androidx.paging.PagingData
import com.application.zaki.movies.domain.model.other.DiscoverItem
import com.application.zaki.movies.domain.usecase.GetDiscover
import com.application.zaki.movies.utils.Category
import com.application.zaki.movies.utils.RxDisposer
import com.application.zaki.movies.utils.addToDisposer
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.BackpressureStrategy
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.ReplaySubject
import javax.inject.Inject

@HiltViewModel
class DiscoverViewModel @Inject constructor(private val getDiscover: GetDiscover) :
    ViewModel() {
    fun getDiscover(
        rxDisposer: RxDisposer,
        genreId: String,
        category: Category
    ): LiveData<PagingData<DiscoverItem>> {
        val subject = ReplaySubject.create<PagingData<DiscoverItem>>()

        getDiscover(genreId, category)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { data ->
                subject.onNext(data)
            }
            .addToDisposer(rxDisposer)

        // convert flowable to livedata
        return LiveDataReactiveStreams.fromPublisher(subject.toFlowable(BackpressureStrategy.BUFFER))
    }
}