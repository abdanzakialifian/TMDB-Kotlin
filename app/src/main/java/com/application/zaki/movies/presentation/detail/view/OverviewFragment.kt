package com.application.zaki.movies.presentation.detail.view

import androidx.fragment.app.viewModels
import com.application.zaki.movies.databinding.FragmentOverviewBinding
import com.application.zaki.movies.presentation.base.BaseVBFragment
import com.application.zaki.movies.presentation.detail.adapter.TrailerAdapter
import com.application.zaki.movies.presentation.detail.viewmodel.DetailViewModel
import com.application.zaki.movies.utils.UiState
import com.application.zaki.movies.utils.gone
import com.application.zaki.movies.utils.setResizableText
import com.application.zaki.movies.utils.visible
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class OverviewFragment : BaseVBFragment<FragmentOverviewBinding>() {

    /*
        To ensure that the viewModel is called after
        the child fragment is attached to the parent/host
    */
    private val detailViewModel by viewModels<DetailViewModel>(ownerProducer = { requireParentFragment() })

    @Inject
    lateinit var trailerAdapter: TrailerAdapter

    override fun getViewBinding(): FragmentOverviewBinding =
        FragmentOverviewBinding.inflate(layoutInflater)

    override fun initView() {
        observeData()
    }

    private fun observeData() {
        detailViewModel.detailDataState.observe(viewLifecycleOwner) { result ->
            when (result) {
                is UiState.Loading -> {
                    binding?.apply {
                        shimmerOverview.visible()
                        shimmerOverview.startShimmer()
                        layoutOverview.gone()
                    }
                }

                is UiState.Success -> {
                    val detail = result.data
                    binding?.apply {
                        shimmerOverview.gone()
                        shimmerOverview.stopShimmer()
                        layoutOverview.visible()
                        tvOverview.setResizableText(detail.overview ?: "", 3, true, layoutText)
                        if (detail.videos.isNullOrEmpty()) {
                            tvTrailer.gone()
                            rvTrailer.gone()
                        } else {
                            tvTrailer.visible()
                            rvTrailer.visible()
                            trailerAdapter.submitList(detail.videos)
                            trailerAdapter.setLifecycleOwner(viewLifecycleOwner)
                            rvTrailer.adapter = trailerAdapter
                            rvTrailer.setHasFixedSize(true)
                        }
                    }
                }

                is UiState.Error -> {
                    binding?.apply {
                        shimmerOverview.gone()
                        shimmerOverview.stopShimmer()
                        layoutOverview.gone()
                    }
                }
                is UiState.Empty -> {
                    binding?.apply {
                        shimmerOverview.gone()
                        shimmerOverview.stopShimmer()
                        layoutOverview.gone()
                    }
                }
            }
        }
    }
}