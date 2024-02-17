package com.application.tmdb.presentation.castcrew.view

import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.application.tmdb.R
import com.application.tmdb.databinding.FragmentDetailCastCrewBinding
import com.application.tmdb.presentation.base.BaseVBFragment
import com.application.tmdb.presentation.castcrew.viewmodel.DetailCastCrewViewModel
import com.application.tmdb.utils.AppBarStateChangedListener
import com.application.tmdb.utils.Category
import com.application.tmdb.utils.RxDisposer
import com.application.tmdb.utils.State
import com.application.tmdb.utils.UiState
import com.application.tmdb.utils.gone
import com.application.tmdb.utils.loadImageUrl
import com.application.tmdb.utils.setResizableText
import com.application.tmdb.utils.visible
import com.google.android.material.appbar.AppBarLayout
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailCastCrewFragment : BaseVBFragment<FragmentDetailCastCrewBinding>() {

    private val args: DetailCastCrewFragmentArgs by navArgs()

    private val detailCastCrewViewModel by viewModels<DetailCastCrewViewModel>()

    override fun getViewBinding(): FragmentDetailCastCrewBinding =
        FragmentDetailCastCrewBinding.inflate(layoutInflater)

    override fun initView() {
        if (args.navigateFrom == Category.CAST) {
            detailCastCrewViewModel.detailCast(
                args.id,
                RxDisposer().apply { bind(viewLifecycleOwner.lifecycle) }
            )
        }
        observeData()
        setAppBarLayout()
    }

    private fun observeData() {
        detailCastCrewViewModel.detailDataState.observe(viewLifecycleOwner) { result ->
            when (result) {
                is UiState.Empty -> {}
                is UiState.Error -> {}
                is UiState.Loading -> {}
                is UiState.Success -> {
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
            appBarLayout.addOnOffsetChangedListener(object : AppBarStateChangedListener() {
                override fun onStateChanged(appBarLayout: AppBarLayout?, state: State?) {
                    if (state == State.COLLAPSED) {
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