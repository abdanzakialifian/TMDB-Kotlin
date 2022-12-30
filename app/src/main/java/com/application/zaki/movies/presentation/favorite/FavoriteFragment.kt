package com.application.zaki.movies.presentation.favorite

import com.application.zaki.movies.databinding.FragmentFavoriteBinding
import com.application.zaki.movies.presentation.base.BaseVBFragment

class FavoriteFragment : BaseVBFragment<FragmentFavoriteBinding>() {
    override fun getViewBinding(): FragmentFavoriteBinding =
        FragmentFavoriteBinding.inflate(layoutInflater)

    override fun initView() {}
}