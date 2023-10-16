package com.application.zaki.movies.presentation.castcrew.view

import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.application.zaki.movies.R
import com.application.zaki.movies.databinding.FragmentDetailCastCrewBinding
import com.application.zaki.movies.presentation.base.BaseVBFragment
import com.application.zaki.movies.presentation.castcrew.viewmodel.DetailCastCrewViewModel
import com.application.zaki.movies.utils.AppBarStateChangedListener
import com.application.zaki.movies.utils.Category
import com.application.zaki.movies.utils.RxDisposer
import com.application.zaki.movies.utils.State
import com.application.zaki.movies.utils.UiState
import com.application.zaki.movies.utils.gone
import com.application.zaki.movies.utils.loadImageUrl
import com.application.zaki.movies.utils.visible
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
                        imgPeople.loadImageUrl(detailCastCrew.profilePath ?: "")
                        collapsingToolbarLayout.title = detailCastCrew.name
                        tvPlaceOfBirth.text = detailCastCrew.placeOfBirth
                        tvBiography.text = detailCastCrew.biography
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