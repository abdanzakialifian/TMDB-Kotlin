package com.application.tmdb.presentation.detail.view

import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import com.application.tmdb.databinding.FragmentSimilarBinding
import com.application.tmdb.domain.model.MovieTvShowModel
import com.application.tmdb.presentation.base.BaseVBFragment
import com.application.tmdb.presentation.detail.viewmodel.DetailViewModel
import com.application.tmdb.presentation.movietvshow.adapter.MovieTvShowPagingAdapter
import com.application.tmdb.utils.Category
import com.application.tmdb.utils.Page
import com.application.tmdb.utils.RxDisposer
import com.application.tmdb.utils.gone
import com.application.tmdb.utils.visible
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SimilarFragment : BaseVBFragment<FragmentSimilarBinding>(),
    MovieTvShowPagingAdapter.OnItemClickCallback {

    /*
        To ensure that the viewModel is called after
        the child fragment is attached to the parent/host
    */
    private val detailViewModel by viewModels<DetailViewModel>(ownerProducer = { requireParentFragment() })


    private val movieTvShowPagingAdapter by lazy { MovieTvShowPagingAdapter() }

    override fun getViewBinding(): FragmentSimilarBinding =
        FragmentSimilarBinding.inflate(layoutInflater)

    override fun initView() {
        observeData()
    }

    private fun observeData() {
        detailViewModel.listSimilarPaging.observe(viewLifecycleOwner) { result ->
            movieTvShowPagingAdapter.submitData(viewLifecycleOwner.lifecycle, result)
            movieTvShowPagingAdapter.setOnItemClickCallback(this)
            binding?.apply {
                rvMovieTvShow.adapter = movieTvShowPagingAdapter
                rvMovieTvShow.setHasFixedSize(true)
            }
            movieTvShowPagingAdapter.addLoadStateListener { loadState ->
                setLoadStatePaging(loadState)
            }
        }
    }

    private fun setLoadStatePaging(loadState: CombinedLoadStates) {
        when (loadState.refresh) {
            is LoadState.Loading -> binding?.apply {
                shimmerList.visible()
                shimmerList.startShimmer()
                rvMovieTvShow.gone()
            }

            is LoadState.NotLoading -> binding?.apply {
                shimmerList.gone()
                shimmerList.stopShimmer()
                rvMovieTvShow.visible()
            }

            is LoadState.Error -> binding?.apply {
                shimmerList.gone()
                shimmerList.stopShimmer()
                rvMovieTvShow.gone()
            }
        }
    }

    private fun navigateToDetailPage(id: Int) {
        detailViewModel.detailData.observe(viewLifecycleOwner) { pair ->
            val intentFrom = pair.first
            val navigateToDetailFragment =
                DetailFragmentDirections.actionDetailFragmentToDetailFragment()
            navigateToDetailFragment.id = id
            navigateToDetailFragment.intentFrom = intentFrom
            findNavController().navigate(navigateToDetailFragment)
        }
    }

    override fun onItemClicked(data: MovieTvShowModel?) {
        navigateToDetailPage(data?.id ?: 0)
    }

    override fun onResume() {
        super.onResume()
        if (detailViewModel.listSimilarPaging.value == null) {
            detailViewModel.detailData.observe(viewLifecycleOwner) { pair ->
                val intentFrom = pair.first
                val detail = pair.second

                if (intentFrom == Category.MOVIES.name) {
                    detailViewModel.getSimilarMovies(
                        null,
                        Page.MORE_THAN_ONE,
                        null,
                        detail.id,
                        RxDisposer().apply { bind(viewLifecycleOwner.lifecycle) }
                    )
                } else {
                    detailViewModel.getSimilarTvShows(
                        null,
                        Page.MORE_THAN_ONE,
                        null,
                        detail.id,
                        RxDisposer().apply { bind(viewLifecycleOwner.lifecycle) }
                    )
                }
            }
        }
    }
}