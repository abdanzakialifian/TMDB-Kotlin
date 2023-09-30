package com.application.zaki.movies.presentation.detail.view

import com.application.zaki.movies.databinding.FragmentOverviewBinding
import com.application.zaki.movies.presentation.base.BaseVBFragment

class OverviewFragment : BaseVBFragment<FragmentOverviewBinding>() {
    override fun getViewBinding(): FragmentOverviewBinding =
        FragmentOverviewBinding.inflate(layoutInflater)

    override fun initView() {}
}