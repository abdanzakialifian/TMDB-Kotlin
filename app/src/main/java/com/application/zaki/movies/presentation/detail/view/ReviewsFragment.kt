package com.application.zaki.movies.presentation.detail.view

import androidx.fragment.app.viewModels
import com.application.zaki.movies.databinding.FragmentReviewsBinding
import com.application.zaki.movies.presentation.base.BaseVBFragment
import com.application.zaki.movies.presentation.detail.viewmodel.DetailViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ReviewsFragment : BaseVBFragment<FragmentReviewsBinding>() {

    private val viewModel by viewModels<DetailViewModel>()

    override fun getViewBinding(): FragmentReviewsBinding =
        FragmentReviewsBinding.inflate(layoutInflater)

    override fun initView() {
//        viewModel.reviewsPaging(
//            RxDisposer().apply { bind(lifecycle) },
//            args.id,
//            Page.MORE_THAN_ONE,
//            if (args.intentFrom == DetailFragment.INTENT_FROM_MOVIE) Category.MOVIES else Category.TV_SHOWS
//        ).observe(viewLifecycleOwner) { result ->
//            val adapter = ReviewsMoviesPagingAdapter()
//            adapter.submitData(lifecycle, result)
//            adapter.addLoadStateListener { loadState ->
//                when (loadState.refresh) {
//                    is LoadState.Loading -> {}
//                    is LoadState.NotLoading -> {
//                        binding?.apply {
//                            rvReviews.adapter = adapter
//                            rvReviews.setHasFixedSize(true)
//                        }
//                    }
//                    is LoadState.Error -> {}
//                }
//            }
//        }
    }
}