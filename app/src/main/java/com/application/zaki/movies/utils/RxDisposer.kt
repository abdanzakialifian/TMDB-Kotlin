package com.application.zaki.movies.utils

import android.util.Log
import androidx.lifecycle.*
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import javax.inject.Inject

// custom disposable rxjava with design pattern mvvm
class RxDisposer @Inject constructor() : DefaultLifecycleObserver {
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

    override fun onDestroy(owner: LifecycleOwner) {
        Log.d("CEK", "ON DESTROY")
        compositeDisposable?.dispose()
        super.onDestroy(owner)
    }

    override fun onPause(owner: LifecycleOwner) {
        Log.d("CEK", "ON PAUSE")
        compositeDisposable?.clear()
        super.onPause(owner)
    }
}

fun Disposable.addToDisposer(rxDisposer: RxDisposer) {
    rxDisposer.add(this)
}