package com.application.zaki.movies.presentation.detail.view

import com.application.zaki.movies.databinding.FragmentSimilarBinding
import com.application.zaki.movies.presentation.base.BaseVBFragment

class SimilarFragment : BaseVBFragment<FragmentSimilarBinding>() {
    override fun getViewBinding(): FragmentSimilarBinding =
        FragmentSimilarBinding.inflate(layoutInflater)

    override fun initView() {}
}