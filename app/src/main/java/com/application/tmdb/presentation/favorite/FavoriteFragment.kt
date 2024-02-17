package com.application.tmdb.presentation.favorite

import com.application.tmdb.databinding.FragmentFavoriteBinding
import com.application.tmdb.presentation.base.BaseVBFragment

class FavoriteFragment : BaseVBFragment<FragmentFavoriteBinding>() {
    override fun getViewBinding(): FragmentFavoriteBinding =
        FragmentFavoriteBinding.inflate(layoutInflater)

    override fun initView() {}
}