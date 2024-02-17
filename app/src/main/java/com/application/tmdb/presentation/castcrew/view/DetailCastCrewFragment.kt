package com.application.tmdb.presentation.castcrew.view

import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.application.tmdb.R
import com.application.tmdb.databinding.FragmentDetailCastCrewBinding
import com.application.tmdb.presentation.base.BaseVBFragment
import com.application.tmdb.presentation.castcrew.viewmodel.DetailCastCrewViewModel
import com.application.tmdb.common.AppBarStateChangedListener
import com.application.tmdb.common.Category
import com.application.tmdb.common.RxDisposer
import com.application.tmdb.common.State
import com.application.tmdb.common.UiState
import com.application.tmdb.common.gone
import com.application.tmdb.common.loadImageUrl
import com.application.tmdb.common.setResizableText
import com.application.tmdb.common.visible
import com.google.android.material.appbar.AppBarLayout
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailCastCrewFragment : BaseVBFragment<FragmentDetailCastCrewBinding>() {

    private val args: DetailCastCrewFragmentArgs by navArgs()

    private val detailCastCrewViewModel by viewModels<DetailCastCrewViewModel>()

    override fun getViewBinding(): FragmentDetailCastCrewBinding =
        FragmentDetailCastCrewBinding.inflate(layoutInflater)

    override fun initView() {
        if (args.navigateFrom == com.application.tmdb.common.Category.CAST) {
            detailCastCrewViewModel.detailCast(
                args.id,
                com.application.tmdb.common.RxDisposer().apply { bind(viewLifecycleOwner.lifecycle) }
            )
        }
        observeData()
        setAppBarLayout()
    }

    private fun observeData() {
        detailCastCrewViewModel.detailDataState.observe(viewLifecycleOwner) { result ->
            when (result) {
                is com.application.tmdb.common.UiState.Empty -> {}
                is com.application.tmdb.common.UiState.Error -> {}
                is com.application.tmdb.common.UiState.Loading -> {}
                is com.application.tmdb.common.UiState.Success -> {
                    val detailCastCrew = result.data
                    binding?.apply {
                        tvBiography.setResizableText(
                            detailCastCrew.biography ?: "",
                            3,
                            true,
                            layoutText
                        )
                        imgPeople.loadImageUrl(detailCastCrew.profilePath ?: "")
                        collapsingToolbarLayout.title = detailCastCrew.name
                        tvPlaceOfBirth.text = detailCastCrew.placeOfBirth
                    }
                }
            }
        }
    }

    private fun setAppBarLayout() {
        binding?.apply {
            appBarLayout.addOnOffsetChangedListener(object : com.application.tmdb.common.AppBarStateChangedListener() {
                override fun onStateChanged(appBarLayout: AppBarLayout?, state: com.application.tmdb.common.State?) {
                    if (state == com.application.tmdb.common.State.COLLAPSED) {
                        imgBack.visible()
                        toolbar.setBackgroundColor(
                            ContextCompat.getColor(
                                requireContext(),
                                R.color.dark_red
                            )
                        )
                    } else {
                        imgBack.gone()
                        toolbar.setBackgroundColor(
                            ContextCompat.getColor(
                                requireContext(),
                                android.R.color.transparent
                            )
                        )
                    }
                }
            })
        }
    }
}