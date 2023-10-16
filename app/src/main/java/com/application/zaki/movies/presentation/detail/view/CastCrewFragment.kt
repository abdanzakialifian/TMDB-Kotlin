package com.application.zaki.movies.presentation.detail.view

import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.application.zaki.movies.R
import com.application.zaki.movies.databinding.FragmentCastCrewBinding
import com.application.zaki.movies.domain.model.CastCrewModel
import com.application.zaki.movies.presentation.base.BaseVBFragment
import com.application.zaki.movies.presentation.detail.adapter.CastCrewAdapter
import com.application.zaki.movies.presentation.detail.viewmodel.DetailViewModel
import com.application.zaki.movies.utils.Category
import com.application.zaki.movies.utils.UiState
import com.application.zaki.movies.utils.gone
import com.application.zaki.movies.utils.visible
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
                is UiState.Loading -> {
                    binding?.apply {
                        shimmerCastCrew.visible()
                        shimmerCastCrew.startShimmer()
                        rvCastCrew.gone()
                    }
                }

                is UiState.Success -> {
                    val detail = result.data
                    castCrewModels.clear()
                    castCrewModels.addAll(
                        listOf(
                            CastCrewModel(
                                id = 1,
                                title = resources.getString(R.string.cast),
                                castCrews = detail.cast,
                                category = Category.CAST
                            ),
                            CastCrewModel(
                                id = 2,
                                title = resources.getString(R.string.crew),
                                castCrews = detail.crew,
                                category = Category.CREW
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

                is UiState.Error -> {
                    binding?.apply {
                        shimmerCastCrew.gone()
                        shimmerCastCrew.stopShimmer()
                        rvCastCrew.gone()
                    }
                }

                is UiState.Empty -> {
                    binding?.apply {
                        shimmerCastCrew.gone()
                        shimmerCastCrew.stopShimmer()
                        rvCastCrew.gone()
                    }
                }
            }
        }
    }

    private fun navigateToDetailCastCrew(id: Int, category: Category) {
        val navigateToDetailCastCrewFragment =
            DetailFragmentDirections.actionDetailFragmentToDetailCastCrewFragment()
        navigateToDetailCastCrewFragment.navigateFrom = category
        navigateToDetailCastCrewFragment.id = id
        findNavController().navigate(navigateToDetailCastCrewFragment)
    }

    override fun onItemClicked(id: Int, category: Category) {
        navigateToDetailCastCrew(id, category)
    }
}