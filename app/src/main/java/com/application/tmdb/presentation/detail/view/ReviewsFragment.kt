package com.application.tmdb.presentation.detail.view

import androidx.fragment.app.viewModels
import androidx.paging.LoadState
import com.application.tmdb.common.utils.Category
import com.application.tmdb.common.utils.RxDisposer
import com.application.tmdb.common.utils.gone
import com.application.tmdb.common.utils.visible
import com.application.tmdb.databinding.FragmentReviewsBinding
import com.application.tmdb.presentation.base.BaseVBFragment
import com.application.tmdb.presentation.detail.adapter.ReviewsPagingAdapter
import com.application.tmdb.presentation.detail.viewmodel.DetailViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ReviewsFragment : BaseVBFragment<FragmentReviewsBinding>() {

    /*
        To ensure that the viewModel is called after
        the child fragment is attached to the parent/host
    */
    private val detailViewModel by viewModels<DetailViewModel>(ownerProducer = { requireParentFragment() })

    private val reviewsPagingAdapter by lazy { ReviewsPagingAdapter() }

    override fun getViewBinding(): FragmentReviewsBinding =
        FragmentReviewsBinding.inflate(layoutInflater)

    override fun initView() {
        observeData()
    }

    private fun observeData() {
        detailViewModel.listReviewsPaging.observe(viewLifecycleOwner) { result ->
            reviewsPagingAdapter.submitData(lifecycle, result)
            reviewsPagingAdapter.addLoadStateListener { loadState ->
                when (loadState.refresh) {
                    is LoadState.Loading -> {
                        binding?.apply {
                            shimmerReviews.visible()
                            shimmerReviews.startShimmer()
                            rvReviews.gone()
                        }
                    }

                    is LoadState.NotLoading -> {
                        binding?.apply {
                            shimmerReviews.gone()
                            shimmerReviews.stopShimmer()
                            rvReviews.visible()
                            rvReviews.adapter = reviewsPagingAdapter
                            rvReviews.setHasFixedSize(true)
                        }
                    }

                    is LoadState.Error -> {
                        binding?.apply {
                            shimmerReviews.gone()
                            shimmerReviews.stopShimmer()
                            rvReviews.gone()
                        }
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (detailViewModel.listReviewsPaging.value == null) {
            detailViewModel.detailData.observe(viewLifecycleOwner) { pair ->
                val intentFrom = pair.first
                val detail = pair.second
                detailViewModel.reviewsPaging(
                    detail.id?.toString(),
                    if (intentFrom == Category.MOVIES.name) Category.MOVIES else Category.TV_SHOWS,
                    RxDisposer().apply { bind(lifecycle) }
                )
            }
        }
    }
}