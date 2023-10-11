package com.application.zaki.movies.presentation.cast.view

import com.application.zaki.movies.databinding.FragmentCastBinding
import com.application.zaki.movies.presentation.base.BaseVBFragment

class CastFragment : BaseVBFragment<FragmentCastBinding>() {
    override fun getViewBinding(): FragmentCastBinding = FragmentCastBinding.inflate(layoutInflater)

    override fun initView() {}
}