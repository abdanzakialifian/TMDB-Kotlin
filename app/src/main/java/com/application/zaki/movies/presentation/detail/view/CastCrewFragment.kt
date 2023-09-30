package com.application.zaki.movies.presentation.detail.view

import com.application.zaki.movies.databinding.FragmentCastCrewBinding
import com.application.zaki.movies.presentation.base.BaseVBFragment

class CastCrewFragment : BaseVBFragment<FragmentCastCrewBinding>() {
    override fun getViewBinding(): FragmentCastCrewBinding =
        FragmentCastCrewBinding.inflate(layoutInflater)

    override fun initView() {}
}