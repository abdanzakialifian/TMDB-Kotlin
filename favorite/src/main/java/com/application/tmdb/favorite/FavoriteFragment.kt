package com.application.tmdb.favorite

import com.application.tmdb.common.base.BaseVBFragment
import com.application.tmdb.favorite.databinding.FragmentFavoriteBinding

class FavoriteFragment : BaseVBFragment<FragmentFavoriteBinding>() {
    override fun getViewBinding(): FragmentFavoriteBinding =
        FragmentFavoriteBinding.inflate(layoutInflater)

    override fun initView() {}
}