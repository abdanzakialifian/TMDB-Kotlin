package com.application.zaki.movies.utils

import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

// custom disposable rxjava with design pattern mvvm
class RxDisposer : LifecycleObserver {
    private var compositeDisposable: CompositeDisposable? = null

    fun bind(lifecycle: Lifecycle) {
        lifecycle.addObserver(this)
        compositeDisposable = CompositeDisposable()
    }

    fun add(disposable: Disposable) {
        compositeDisposable?.add(disposable) ?: kotlin.run {
            throw NullPointerException("No lifecycle bound")
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy() {
        Log.d("CEK", "ON DESTROY")
        compositeDisposable?.dispose()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun onPause() {
        Log.d("CEK","ON PAUSE")
        compositeDisposable?.clear()
    }
}

fun Disposable.addToDisposer(rxDisposer: RxDisposer) {
    rxDisposer.add(this)
}