package com.application.tmdb.presentation.detail.view

import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.application.tmdb.R
import com.application.tmdb.databinding.FragmentCastCrewBinding
import com.application.tmdb.core.domain.model.CastCrewModel
import com.application.tmdb.presentation.base.BaseVBFragment
import com.application.tmdb.presentation.detail.adapter.CastCrewAdapter
import com.application.tmdb.presentation.detail.viewmodel.DetailViewModel
import com.application.tmdb.common.Category
import com.application.tmdb.common.UiState
import com.application.tmdb.common.gone
import com.application.tmdb.common.visible
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CastCrewFragment : BaseVBFragment<FragmentCastCrewBinding>(),
    CastCrewAdapter.OnItemClickCallback {

    /*
        To ensure that the viewModel is called after
        the child fragment is attached to the parent/host
    */
    private val detailViewModel by viewModels<DetailViewModel>(ownerProducer = { requireParentFragment() })

    private val castCrewAdapter by lazy { CastCrewAdapter() }

    private val castCrewModels = mutableListOf<CastCrewModel>()

    override fun getViewBinding(): FragmentCastCrewBinding =
        FragmentCastCrewBinding.inflate(layoutInflater)

    override fun initView() {
        observeData()
    }

    private fun observeData() {
        detailViewModel.detailDataState.observe(viewLifecycleOwner) { result ->
            when (result) {
                is com.application.tmdb.common.UiState.Loading -> {
                    binding?.apply {
                        shimmerCastCrew.visible()
                        shimmerCastCrew.startShimmer()
                        rvCastCrew.gone()
                    }
                }

                is com.application.tmdb.common.UiState.Success -> {
                    val detail = result.data
                    castCrewModels.clear()
                    castCrewModels.addAll(
                        listOf(
                            CastCrewModel(
                                id = 1,
                                title = resources.getString(R.string.cast),
                                castCrews = detail.cast,
                                category = com.application.tmdb.common.Category.CAST
                            ),
                            CastCrewModel(
                                id = 2,
                                title = resources.getString(R.string.crew),
                                castCrews = detail.crew,
                                category = com.application.tmdb.common.Category.CREW
                            )
                        )
                    )
                    castCrewAdapter.submitList(castCrewModels)
                    castCrewAdapter.setOnItemClickCallback(this)
                    binding?.apply {
                        shimmerCastCrew.gone()
                        shimmerCastCrew.stopShimmer()
                        rvCastCrew.visible()
                        rvCastCrew.adapter = castCrewAdapter
                        rvCastCrew.setHasFixedSize(true)
                    }
                }

                is com.application.tmdb.common.UiState.Error -> {
                    binding?.apply {
                        shimmerCastCrew.gone()
                        shimmerCastCrew.stopShimmer()
                        rvCastCrew.gone()
                    }
                }

                is com.application.tmdb.common.UiState.Empty -> {
                    binding?.apply {
                        shimmerCastCrew.gone()
                        shimmerCastCrew.stopShimmer()
                        rvCastCrew.gone()
                    }
                }
            }
        }
    }

    private fun navigateToDetailCastCrew(id: Int, category: com.application.tmdb.common.Category) {
        val navigateToDetailCastCrewFragment =
            DetailFragmentDirections.actionDetailFragmentToDetailCastCrewFragment()
        navigateToDetailCastCrewFragment.navigateFrom = category
        navigateToDetailCastCrewFragment.id = id
        findNavController().navigate(navigateToDetailCastCrewFragment)
    }

    override fun onItemClicked(id: Int, category: com.application.tmdb.common.Category) {
        navigateToDetailCastCrew(id, category)
    }
}