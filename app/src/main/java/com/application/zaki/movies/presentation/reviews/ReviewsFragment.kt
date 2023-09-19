package com.application.zaki.movies.presentation.reviews

import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.paging.LoadState
import com.application.zaki.movies.data.source.remote.paging.other.ReviewsRxPagingSource
import com.application.zaki.movies.databinding.FragmentReviewsBinding
import com.application.zaki.movies.presentation.base.BaseVBFragment
import com.application.zaki.movies.presentation.detail.adapter.ReviewsMoviesPagingAdapter
import com.application.zaki.movies.presentation.detail.view.DetailFragment
import com.application.zaki.movies.presentation.detail.viewmodel.DetailViewModel
import com.application.zaki.movies.utils.Category
import com.application.zaki.movies.utils.Page
import com.application.zaki.movies.utils.RxDisposer
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ReviewsFragment : BaseVBFragment<FragmentReviewsBinding>() {

    private val viewModel by viewModels<DetailViewModel>()
    private val args: ReviewsFragmentArgs by navArgs()

    override fun getViewBinding(): FragmentReviewsBinding =
        FragmentReviewsBinding.inflate(layoutInflater)

    override fun initView() {
        viewModel.reviewsPaging(
            RxDisposer().apply { bind(lifecycle) },
            args.id,
            Page.MORE_THAN_ONE,
            if (args.intentFrom == DetailFragment.INTENT_FROM_MOVIE) Category.MOVIES else Category.TV_SHOWS
        ).observe(viewLifecycleOwner) { result ->
            val adapter = ReviewsMoviesPagingAdapter()
            adapter.submitData(lifecycle, result)
            adapter.addLoadStateListener { loadState ->
                when (loadState.refresh) {
                    is LoadState.Loading -> {}
                    is LoadState.NotLoading -> {
                        binding?.apply {
                            rvReviews.adapter = adapter
                            rvReviews.setHasFixedSize(true)
                        }
                    }
                    is LoadState.Error -> {}
                }
            }
        }
    }
}