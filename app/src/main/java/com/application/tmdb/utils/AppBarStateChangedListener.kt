package com.application.tmdb.utils

import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.AppBarLayout.OnOffsetChangedListener
import kotlin.math.abs


abstract class AppBarStateChangedListener : OnOffsetChangedListener {
    private var mCurrentState = State.IDLE
    override fun onOffsetChanged(appBarLayout: AppBarLayout, verticalOffset: Int) {
        if (verticalOffset == 0) {
            setCurrentStateAndNotify(appBarLayout, State.EXPANDED)
        } else if (abs(verticalOffset) >= appBarLayout.totalScrollRange) {
            setCurrentStateAndNotify(appBarLayout, State.COLLAPSED)
        } else {
            setCurrentStateAndNotify(appBarLayout, State.IDLE)
        }
    }

    private fun setCurrentStateAndNotify(appBarLayout: AppBarLayout, state: State) {
        if (mCurrentState != state) {
            onStateChanged(appBarLayout, state)
        }
        mCurrentState = state
    }

    abstract fun onStateChanged(appBarLayout: AppBarLayout?, state: State?)
}