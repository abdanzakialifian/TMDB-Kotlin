package com.application.zaki.movies.presentation.list.view

import android.util.Log
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.application.zaki.movies.databinding.FragmentListBinding
import com.application.zaki.movies.domain.model.movies.ListPopularMovies
import com.application.zaki.movies.domain.model.movies.ListTopRatedMovies
import com.application.zaki.movies.domain.model.movies.ListUpComingMovies
import com.application.zaki.movies.domain.model.tvshows.ListOnTheAirTvShows
import com.application.zaki.movies.domain.model.tvshows.ListPopularTvShows
import com.application.zaki.movies.domain.model.tvshows.ListTopRatedTvShows
import com.application.zaki.movies.presentation.base.BaseVBFragment
import com.application.zaki.movies.presentation.detail.view.DetailFragment
import com.application.zaki.movies.presentation.list.adapter.movies.PopularMoviesPagingAdapter
import com.application.zaki.movies.presentation.list.adapter.movies.TopRatedMoviesPagingAdapter
import com.application.zaki.movies.presentation.list.adapter.movies.UpComingMoviesPagingAdapter
import com.application.zaki.movies.presentation.list.adapter.tvshows.OnTheAirTvShowsPagingAdapter
import com.application.zaki.movies.presentation.list.adapter.tvshows.PopularTvShowsPagingAdapter
import com.application.zaki.movies.presentation.list.adapter.tvshows.TopRatedTvShowsPagingAdapter
import com.application.zaki.movies.presentation.movies.viewmodel.MoviesViewModel
import com.application.zaki.movies.presentation.tvshows.viewmodel.TvShowsViewModel
import com.application.zaki.movies.utils.RxDisposer
import com.application.zaki.movies.utils.UiState
import com.application.zaki.movies.utils.gone
import com.application.zaki.movies.utils.visible
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ListFragment : BaseVBFragment<FragmentListBinding>() {

    private val args: ListFragmentArgs by navArgs()
    private val moviesViewModel by viewModels<MoviesViewModel>()
    private val tvShowsViewModel by viewModels<TvShowsViewModel>()

    override fun getViewBinding(): FragmentListBinding =
        FragmentListBinding.inflate(layoutInflater)

    override fun initView() {
        when (args.intentFrom) {
            // set list from movies
            INTENT_FROM_TOP_RATED_MOVIES -> setListTopRatedMoviesPaging()
            INTENT_FROM_POPULAR_MOVIES -> setListPopularMoviesPaging()
            INTENT_FROM_UP_COMING_MOVIES -> setListUpComingMoviesPaging()
            // set list from tv shows
            INTENT_FROM_TOP_RATED_TV_SHOWS -> setListTopRatedTvShowsPaging()
            INTENT_FROM_POPULAR_TV_SHOWS -> setListPopularTvShowsPaging()
            INTENT_FROM_ON_THE_AIR_TV_SHOWS -> setListOnTheAirTvShowsPaging()
        }
    }

    private fun setListTopRatedMoviesPaging() {
        moviesViewModel.topRatedMoviesPaging(RxDisposer().apply { bind(lifecycle) })
            .observe(viewLifecycleOwner) { result ->
                val adapter =
                    TopRatedMoviesPagingAdapter(object :
                        TopRatedMoviesPagingAdapter.OnItemClickCallback {
                        override fun onItemClicked(data: ListTopRatedMovies?) {
                            navigateToDetailFragment(data?.id ?: 0)
                        }
                    })
                adapter.submitData(lifecycle, result)
                binding?.apply {
                    rvListMovies.adapter = adapter
                    rvListMovies.setHasFixedSize(true)
                }
                adapter.addLoadStateListener { loadState ->
                    when (loadState.refresh) {
                        is LoadState.Loading -> {
                            Log.d("LOG", "setReviews: LOADING")
                            showLoading()
                        }
                        is LoadState.NotLoading -> {
                            Log.d("LOG", "setReviews: MASUK")
                            visibleDataList()
                        }
                        is LoadState.Error -> {
                            Log.d("LOG", "setReviews: ERROR")
                            goneDataList()
                        }
                    }
                }
            }
    }

    private fun setListPopularMoviesPaging() {
        moviesViewModel.popularMoviesPaging(RxDisposer().apply { bind(lifecycle) })
            .observe(viewLifecycleOwner) { result ->
                val adapter =
                    PopularMoviesPagingAdapter(object :
                        PopularMoviesPagingAdapter.OnItemClickCallback {
                        override fun onItemClicked(data: ListPopularMovies?) {
                            navigateToDetailFragment(data?.id ?: 0)
                        }
                    })
                adapter.submitData(lifecycle, result)
                binding?.apply {
                    rvListMovies.adapter = adapter
                    rvListMovies.setHasFixedSize(true)
                }
                adapter.addLoadStateListener { loadState ->
                    when (loadState.refresh) {
                        is LoadState.Loading -> {
                            Log.d("LOG", "setReviews: LOADING")
                            showLoading()
                        }
                        is LoadState.NotLoading -> {
                            Log.d("LOG", "setReviews: MASUK")
                            visibleDataList()
                        }
                        is LoadState.Error -> {
                            Log.d("LOG", "setReviews: ERROR")
                            goneDataList()
                        }
                    }
                }
            }
    }

    private fun setListUpComingMoviesPaging() {
        moviesViewModel.upComingMoviesPaging(RxDisposer().apply { bind(lifecycle) })
            .observe(viewLifecycleOwner) { result ->
                val adapter =
                    UpComingMoviesPagingAdapter(object :
                        UpComingMoviesPagingAdapter.OnItemClickCallback {
                        override fun onItemClicked(data: ListUpComingMovies?) {
                            navigateToDetailFragment(data?.id ?: 0)
                        }
                    })
                adapter.submitData(lifecycle, result)
                binding?.apply {
                    rvListMovies.adapter = adapter
                    rvListMovies.setHasFixedSize(true)
                }
                adapter.addLoadStateListener { loadState ->
                    when (loadState.refresh) {
                        is LoadState.Loading -> {
                            Log.d("LOG", "setReviews: LOADING")
                            showLoading()
                        }
                        is LoadState.NotLoading -> {
                            Log.d("LOG", "setReviews: MASUK")
                            visibleDataList()
                        }
                        is LoadState.Error -> {
                            Log.d("LOG", "setReviews: ERROR")
                            goneDataList()
                        }
                    }
                }
            }
    }

    private fun setListTopRatedTvShowsPaging() {
        tvShowsViewModel.topRatedTvShowsPaging(RxDisposer().apply { bind(lifecycle) })
            .observe(viewLifecycleOwner) { result ->
                val adapter =
                    TopRatedTvShowsPagingAdapter(object : TopRatedTvShowsPagingAdapter.OnItemClickCallback {
                        override fun onItemClicked(data: ListTopRatedTvShows?) {
                            navigateToDetailFragment(data?.id ?: 0)
                        }
                    })
                adapter.submitData(lifecycle, result)
                binding?.apply {
                    rvListMovies.adapter = adapter
                    rvListMovies.setHasFixedSize(true)
                }
                adapter.addLoadStateListener { loadState ->
                    when (loadState.refresh) {
                        is LoadState.Loading -> {
                            Log.d("LOG", "setReviews: LOADING")
                            showLoading()
                        }
                        is LoadState.NotLoading -> {
                            Log.d("LOG", "setReviews: MASUK")
                            visibleDataList()
                        }
                        is LoadState.Error -> {
                            Log.d("LOG", "setReviews: ERROR")
                            goneDataList()
                        }
                    }
                }
            }
    }

    private fun setListPopularTvShowsPaging() {
        tvShowsViewModel.popularTvShowsPaging(RxDisposer().apply { bind(lifecycle) })
            .observe(viewLifecycleOwner) { result ->
                val adapter =
                    PopularTvShowsPagingAdapter(object : PopularTvShowsPagingAdapter.OnItemClickCallback {
                        override fun onItemClicked(data: ListPopularTvShows?) {
                            navigateToDetailFragment(data?.id ?: 0)
                        }
                    })
                adapter.submitData(lifecycle, result)
                binding?.apply {
                    rvListMovies.adapter = adapter
                    rvListMovies.setHasFixedSize(true)
                }
                adapter.addLoadStateListener { loadState ->
                    when (loadState.refresh) {
                        is LoadState.Loading -> {
                            Log.d("LOG", "setReviews: LOADING")
                            showLoading()
                        }
                        is LoadState.NotLoading -> {
                            Log.d("LOG", "setReviews: MASUK")
                            visibleDataList()
                        }
                        is LoadState.Error -> {
                            Log.d("LOG", "setReviews: ERROR")
                            goneDataList()
                        }
                    }
                }
            }
    }

    private fun setListOnTheAirTvShowsPaging() {
        tvShowsViewModel.onTheAirTvShowsPaging(RxDisposer().apply { bind(lifecycle) })
            .observe(viewLifecycleOwner) { result ->
                val adapter =
                    OnTheAirTvShowsPagingAdapter(object :
                        OnTheAirTvShowsPagingAdapter.OnItemClickCallback {
                        override fun onItemClicked(data: ListOnTheAirTvShows?) {
                            navigateToDetailFragment(data?.id ?: 0)
                        }
                    })
                adapter.submitData(lifecycle, result)
                binding?.apply {
                    rvListMovies.adapter = adapter
                    rvListMovies.setHasFixedSize(true)
                }
                adapter.addLoadStateListener {
                    adapter.addLoadStateListener { loadState ->
                        when (loadState.refresh) {
                            is LoadState.Loading -> {
                                Log.d("LOG", "setReviews: LOADING")
                                showLoading()
                            }
                            is LoadState.NotLoading -> {
                                Log.d("LOG", "setReviews: MASUK")
                                visibleDataList()
                            }
                            is LoadState.Error -> {
                                Log.d("LOG", "setReviews: ERROR")
                                goneDataList()
                            }
                        }
                    }
                }
            }
    }

    private fun goneDataList() {
        binding?.apply {
            shimmerList.gone()
            shimmerList.stopShimmer()
            rvListMovies.gone()
        }
    }

    private fun visibleDataList() {
        binding?.apply {
            shimmerList.gone()
            shimmerList.stopShimmer()
            rvListMovies.visible()
        }
    }

    private fun showLoading() {
        binding?.apply {
            shimmerList.visible()
            shimmerList.startShimmer()
            rvListMovies.gone()
        }
    }

    private fun navigateToDetailFragment(id: Int) {
        val navigateToDetailFragment =
            ListFragmentDirections.actionListFragmentToDetailFragment()
        navigateToDetailFragment.id = id
        navigateToDetailFragment.intentFrom = DetailFragment.INTENT_FROM_MOVIE
        findNavController().navigate(navigateToDetailFragment)
    }

    companion object {
        // intent from movies
        const val INTENT_FROM_POPULAR_MOVIES = "List Popular Movies"
        const val INTENT_FROM_TOP_RATED_MOVIES = "Top Rated Movies"
        const val INTENT_FROM_UP_COMING_MOVIES = "List Up Coming Movies"

        // intent from tv shows
        const val INTENT_FROM_ON_THE_AIR_TV_SHOWS = "Intent On The Air TV Shows"
        const val INTENT_FROM_POPULAR_TV_SHOWS = "Intent Popular TV Shows"
        const val INTENT_FROM_TOP_RATED_TV_SHOWS = "Intent Top Rated TV Shows"
    }
}