package com.application.zaki.movies.presentation.search

import com.application.zaki.movies.databinding.FragmentSearchBinding
import com.application.zaki.movies.presentation.base.BaseVBFragment

class SearchFragment : BaseVBFragment<FragmentSearchBinding>() {
    override fun getViewBinding(): FragmentSearchBinding =
        FragmentSearchBinding.inflate(layoutInflater)

    override fun initView() {}
}