package com.application.zaki.movies.presentation.detail.view

import androidx.core.view.ViewCompat
import androidx.fragment.app.viewModels
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.application.zaki.movies.databinding.FragmentSimilarBinding
import com.application.zaki.movies.domain.model.MovieTvShow
import com.application.zaki.movies.presentation.base.BaseVBFragment
import com.application.zaki.movies.presentation.detail.viewmodel.DetailViewModel
import com.application.zaki.movies.presentation.movietvshow.adapter.MovieTvShowPagingAdapter
import com.application.zaki.movies.utils.Category
import com.application.zaki.movies.utils.Page
import com.application.zaki.movies.utils.RxDisposer
import com.application.zaki.movies.utils.gone
import com.application.zaki.movies.utils.visible
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SimilarFragment : BaseVBFragment<FragmentSimilarBinding>(),
    MovieTvShowPagingAdapter.OnItemClickCallback {

    /*
        To ensure that the viewModel is called after
        the child fragment is attached to the parent/host
    */
    private val detailViewModel by viewModels<DetailViewModel>(ownerProducer = { requireParentFragment() })

    @Inject
    lateinit var movieTvShowPagingAdapter: MovieTvShowPagingAdapter

    override fun getViewBinding(): FragmentSimilarBinding =
        FragmentSimilarBinding.inflate(layoutInflater)

    override fun initView() {
        observeData()
    }

    private fun observeData() {
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
            }
        }

        detailViewModel.listMoviesPaging.observe(viewLifecycleOwner) { result ->
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

    override fun onItemClicked(data: MovieTvShow?) {}
}