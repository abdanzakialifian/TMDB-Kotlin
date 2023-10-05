package com.application.zaki.movies.presentation.detail.view

import androidx.fragment.app.viewModels
import androidx.paging.LoadState
import com.application.zaki.movies.databinding.FragmentReviewsBinding
import com.application.zaki.movies.presentation.base.BaseVBFragment
import com.application.zaki.movies.presentation.detail.adapter.ReviewsPagingAdapter
import com.application.zaki.movies.presentation.detail.viewmodel.DetailViewModel
import com.application.zaki.movies.utils.Category
import com.application.zaki.movies.utils.RxDisposer
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ReviewsFragment : BaseVBFragment<FragmentReviewsBinding>() {

    /*
        To ensure that the viewModel is called after
        the child fragment is attached to the parent/host
    */
    private val detailViewModel by viewModels<DetailViewModel>(ownerProducer = { requireParentFragment() })

    @Inject
    lateinit var reviewsPagingAdapter: ReviewsPagingAdapter

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
                    is LoadState.Loading -> {}
                    is LoadState.NotLoading -> {
                        binding?.apply {
                            rvReviews.adapter = reviewsPagingAdapter
                            rvReviews.setHasFixedSize(true)
                        }
                    }

                    is LoadState.Error -> {}
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