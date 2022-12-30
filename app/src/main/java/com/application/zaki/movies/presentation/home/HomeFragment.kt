package com.application.zaki.movies.presentation.home

import com.application.zaki.movies.databinding.FragmentHomeBinding
import com.application.zaki.movies.presentation.base.BaseVBFragment
import com.google.android.material.tabs.TabLayoutMediator

class HomeFragment : BaseVBFragment<FragmentHomeBinding>() {

    override fun getViewBinding(): FragmentHomeBinding = FragmentHomeBinding.inflate(layoutInflater)

    override fun initView() {
        binding?.apply {
            viewPager.isUserInputEnabled = false
            val adapter = ViewPagerAdapter(childFragmentManager, lifecycle)
            viewPager.adapter = adapter

            TabLayoutMediator(tabLayout, viewPager) { tab, position ->
                tab.text = TABS_NAME_LIST[position]
            }.attach()
        }
    }

    companion object {
        private val TABS_NAME_LIST = arrayOf("Movies", "Tv Shows")
    }
}