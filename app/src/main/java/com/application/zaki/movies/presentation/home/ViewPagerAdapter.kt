package com.application.zaki.movies.presentation.home

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.application.zaki.movies.presentation.movies.view.MoviesFragment
import com.application.zaki.movies.presentation.tvshows.view.TvShowsFragment

class ViewPagerAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager, lifecycle) {
    override fun getItemCount(): Int = NUMBER_OF_TABS

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> MoviesFragment()
            else -> TvShowsFragment()
        }
    }

    companion object {
        private const val NUMBER_OF_TABS = 2
    }
}