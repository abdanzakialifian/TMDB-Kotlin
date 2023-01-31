package com.application.zaki.movies.presentation.list.view

import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
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
import com.application.zaki.movies.utils.UiState
import com.application.zaki.movies.utils.RxDisposer
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
        val adapter =
            TopRatedMoviesPagingAdapter(object : TopRatedMoviesPagingAdapter.OnItemClickCallback {
                override fun onItemClicked(data: ListTopRatedMovies?) {
                    val navigateToDetailFragment =
                        ListFragmentDirections.actionListFragmentToDetailFragment()
                    navigateToDetailFragment.id = data?.id ?: 0
                    navigateToDetailFragment.intentFrom = DetailFragment.INTENT_FROM_MOVIE
                    findNavController().navigate(navigateToDetailFragment)
                }
            })
        binding?.rvListMovies?.layoutManager = LinearLayoutManager(requireContext())
        binding?.rvListMovies?.adapter = adapter
        binding?.rvListMovies?.setHasFixedSize(true)
        moviesViewModel.topRatedMoviesPaging(RxDisposer().apply { bind(lifecycle) })
            .observe(viewLifecycleOwner) { result ->
                if (result != null) {
                    when (result) {
                        is UiState.Loading -> {
                            showLoading()
                        }
                        is UiState.Success -> {
                            binding?.apply {
                                adapter.submitData(lifecycle, result.data)
                                visibleDataList()
                            }
                        }
                        is UiState.Error -> {
                            goneDataList()
                        }
                        is UiState.Empty -> {
                            goneDataList()
                        }
                    }
                }
            }
    }

    private fun setListPopularMoviesPaging() {
        val adapter =
            PopularMoviesPagingAdapter(object : PopularMoviesPagingAdapter.OnItemClickCallback {
                override fun onItemClicked(data: ListPopularMovies?) {
                    val navigateToDetailFragment =
                        ListFragmentDirections.actionListFragmentToDetailFragment()
                    navigateToDetailFragment.id = data?.id ?: 0
                    navigateToDetailFragment.intentFrom = DetailFragment.INTENT_FROM_MOVIE
                    findNavController().navigate(navigateToDetailFragment)
                }
            })
        binding?.rvListMovies?.layoutManager = LinearLayoutManager(requireContext())
        binding?.rvListMovies?.adapter = adapter
        binding?.rvListMovies?.setHasFixedSize(true)
        moviesViewModel.popularMoviesPaging(RxDisposer().apply { bind(lifecycle) })
            .observe(viewLifecycleOwner) { result ->
                if (result != null) {
                    when (result) {
                        is UiState.Loading -> {
                            showLoading()
                        }
                        is UiState.Success -> {
                            binding?.apply {
                                adapter.submitData(lifecycle, result.data)
                                visibleDataList()
                            }
                        }
                        is UiState.Error -> {
                            goneDataList()
                        }
                        is UiState.Empty -> {
                            goneDataList()
                        }
                    }
                }
            }
    }

    private fun setListUpComingMoviesPaging() {
        val adapter =
            UpComingMoviesPagingAdapter(object : UpComingMoviesPagingAdapter.OnItemClickCallback {
                override fun onItemClicked(data: ListUpComingMovies?) {
                    val navigateToDetailFragment =
                        ListFragmentDirections.actionListFragmentToDetailFragment()
                    navigateToDetailFragment.id = data?.id ?: 0
                    navigateToDetailFragment.intentFrom = DetailFragment.INTENT_FROM_MOVIE
                    findNavController().navigate(navigateToDetailFragment)
                }
            })
        binding?.rvListMovies?.layoutManager = LinearLayoutManager(requireContext())
        binding?.rvListMovies?.adapter = adapter
        binding?.rvListMovies?.setHasFixedSize(true)
        moviesViewModel.upComingMoviesPaging(RxDisposer().apply { bind(lifecycle) })
            .observe(viewLifecycleOwner) { result ->
                if (result != null) {
                    when (result) {
                        is UiState.Loading -> {
                            showLoading()
                        }
                        is UiState.Success -> {
                            binding?.apply {
                                adapter.submitData(lifecycle, result.data)
                                visibleDataList()
                            }
                        }
                        is UiState.Error -> {
                            goneDataList()
                        }
                        is UiState.Empty -> {
                            goneDataList()
                        }
                    }
                }
            }
    }

    private fun setListTopRatedTvShowsPaging() {
        val adapter =
            TopRatedTvShowsPagingAdapter(object : TopRatedTvShowsPagingAdapter.OnItemClickCallback {
                override fun onItemClicked(data: ListTopRatedTvShows?) {
                    val navigateToDetailFragment =
                        ListFragmentDirections.actionListFragmentToDetailFragment()
                    navigateToDetailFragment.id = data?.id ?: 0
                    navigateToDetailFragment.intentFrom = DetailFragment.INTENT_FROM_TV_SHOWS
                    findNavController().navigate(navigateToDetailFragment)
                }
            })
        binding?.rvListMovies?.layoutManager = LinearLayoutManager(requireContext())
        binding?.rvListMovies?.adapter = adapter
        binding?.rvListMovies?.setHasFixedSize(true)
        tvShowsViewModel.topRatedTvShowsPaging(RxDisposer().apply { bind(lifecycle) })
            .observe(viewLifecycleOwner) { result ->
                if (result != null) {
                    when (result) {
                        is UiState.Loading -> {
                            showLoading()
                        }
                        is UiState.Success -> {
                            binding?.apply {
                                adapter.submitData(lifecycle, result.data)
                                visibleDataList()
                            }
                        }
                        is UiState.Error -> {
                            goneDataList()
                        }
                        is UiState.Empty -> {
                            goneDataList()
                        }
                    }
                }
            }
    }

    private fun setListPopularTvShowsPaging() {
        val adapter =
            PopularTvShowsPagingAdapter(object : PopularTvShowsPagingAdapter.OnItemClickCallback {
                override fun onItemClicked(data: ListPopularTvShows?) {
                    val navigateToDetailFragment =
                        ListFragmentDirections.actionListFragmentToDetailFragment()
                    navigateToDetailFragment.id = data?.id ?: 0
                    navigateToDetailFragment.intentFrom = DetailFragment.INTENT_FROM_TV_SHOWS
                    findNavController().navigate(navigateToDetailFragment)
                }
            })
        binding?.rvListMovies?.layoutManager = LinearLayoutManager(requireContext())
        binding?.rvListMovies?.adapter = adapter
        binding?.rvListMovies?.setHasFixedSize(true)
        tvShowsViewModel.popularTvShowsPaging(RxDisposer().apply { bind(lifecycle) })
            .observe(viewLifecycleOwner) { result ->
                if (result != null) {
                    when (result) {
                        is UiState.Loading -> {
                            showLoading()
                        }
                        is UiState.Success -> {
                            binding?.apply {
                                adapter.submitData(lifecycle, result.data)
                                visibleDataList()
                            }
                        }
                        is UiState.Error -> {
                            goneDataList()
                        }
                        is UiState.Empty -> {
                            goneDataList()
                        }
                    }
                }
            }
    }

    private fun setListOnTheAirTvShowsPaging() {
        val adapter =
            OnTheAirTvShowsPagingAdapter(object : OnTheAirTvShowsPagingAdapter.OnItemClickCallback {
                override fun onItemClicked(data: ListOnTheAirTvShows?) {
                    val navigateToDetailFragment =
                        ListFragmentDirections.actionListFragmentToDetailFragment()
                    navigateToDetailFragment.id = data?.id ?: 0
                    navigateToDetailFragment.intentFrom = DetailFragment.INTENT_FROM_TV_SHOWS
                    findNavController().navigate(navigateToDetailFragment)
                }
            })
        binding?.rvListMovies?.layoutManager = LinearLayoutManager(requireContext())
        binding?.rvListMovies?.adapter = adapter
        binding?.rvListMovies?.setHasFixedSize(true)
        tvShowsViewModel.onTheAirTvShowsPaging(RxDisposer().apply { bind(lifecycle) })
            .observe(viewLifecycleOwner) { result ->
                if (result != null) {
                    when (result) {
                        is UiState.Loading -> {
                            showLoading()
                        }
                        is UiState.Success -> {
                            binding?.apply {
                                adapter.submitData(lifecycle, result.data)
                                visibleDataList()
                            }
                        }
                        is UiState.Error -> {
                            goneDataList()
                        }
                        is UiState.Empty -> {
                            goneDataList()
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