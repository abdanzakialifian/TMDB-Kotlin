package com.application.zaki.movies.presentation.movies.view

import android.os.Handler
import android.os.Looper
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.application.zaki.movies.databinding.FragmentMoviesBinding
import com.application.zaki.movies.domain.model.movies.ListNowPlayingMovies
import com.application.zaki.movies.domain.model.movies.ListPopularMovies
import com.application.zaki.movies.domain.model.movies.ListTopRatedMovies
import com.application.zaki.movies.domain.model.movies.ListUpComingMovies
import com.application.zaki.movies.presentation.base.BaseVBFragment
import com.application.zaki.movies.presentation.detail.view.DetailFragment.Companion.INTENT_FROM_MOVIE
import com.application.zaki.movies.presentation.home.HomeFragmentDirections
import com.application.zaki.movies.presentation.list.view.ListFragment.Companion.INTENT_FROM_POPULAR_MOVIES
import com.application.zaki.movies.presentation.list.view.ListFragment.Companion.INTENT_FROM_TOP_RATED_MOVIES
import com.application.zaki.movies.presentation.list.view.ListFragment.Companion.INTENT_FROM_UP_COMING_MOVIES
import com.application.zaki.movies.presentation.movies.adapter.NowPlayingMoviesAdapter
import com.application.zaki.movies.presentation.movies.adapter.PopularMoviesAdapter
import com.application.zaki.movies.presentation.movies.adapter.TopRatedMoviesAdapter
import com.application.zaki.movies.presentation.movies.adapter.UpComingMoviesAdapter
import com.application.zaki.movies.presentation.movies.viewmodel.MoviesViewModel
import com.application.zaki.movies.utils.NetworkResult
import com.application.zaki.movies.utils.RxDisposer
import com.application.zaki.movies.utils.gone
import com.application.zaki.movies.utils.visible
import dagger.hilt.android.AndroidEntryPoint
import kotlin.math.abs

@AndroidEntryPoint
class MoviesFragment : BaseVBFragment<FragmentMoviesBinding>() {

    private val moviesViewModel by viewModels<MoviesViewModel>()
    private val sliderHandler = Handler(Looper.getMainLooper())
    private lateinit var sliderRunnable: Runnable

    override fun getViewBinding(): FragmentMoviesBinding =
        FragmentMoviesBinding.inflate(layoutInflater)

    override fun initView() {
        sliderRunnable = Runnable {
            binding?.viewPagerImageSlider?.currentItem =
                binding?.viewPagerImageSlider?.currentItem?.plus(1) ?: 0
        }
        setImageSlider()
        setTopRatedMovies()
        setPopularMovies()
        setUpComingMovies()

        binding?.apply {
            tvSeeAllTopRatedMovies.setOnClickListener {
                val navigateToListFragment =
                    HomeFragmentDirections.actionHomeFragmentToListFragment()
                navigateToListFragment.intentFrom = INTENT_FROM_TOP_RATED_MOVIES
                findNavController().navigate(navigateToListFragment)
            }

            tvSeeAllPopularMovies.setOnClickListener {
                val navigateToListFragment =
                    HomeFragmentDirections.actionHomeFragmentToListFragment()
                navigateToListFragment.intentFrom = INTENT_FROM_POPULAR_MOVIES
                findNavController().navigate(navigateToListFragment)
            }
            tvSeeAllUpComingMovies.setOnClickListener {
                val navigateToListFragment =
                    HomeFragmentDirections.actionHomeFragmentToListFragment()
                navigateToListFragment.intentFrom = INTENT_FROM_UP_COMING_MOVIES
                findNavController().navigate(navigateToListFragment)
            }
        }
    }

    private fun setImageSlider() {
        val adapter = NowPlayingMoviesAdapter(object : NowPlayingMoviesAdapter.OnItemClickCallback {
            override fun onItemClicked(data: ListNowPlayingMovies) {
                val navigateToDetailFragment =
                    HomeFragmentDirections.actionHomeFragmentToDetailFragment()
                navigateToDetailFragment.id = data.id ?: 0
                navigateToDetailFragment.intentFrom = INTENT_FROM_MOVIE
                findNavController().navigate(navigateToDetailFragment)
            }
        })
        moviesViewModel.nowPlayingMovies(RxDisposer().apply { bind(lifecycle) })
            .observe(viewLifecycleOwner) { result ->
                if (result != null) {
                    when (result) {
                        is NetworkResult.Loading -> {
                            binding?.apply {
                                shimmerImageSlider.startShimmer()
                                shimmerImageSlider.visible()
                                viewPagerImageSlider.gone()
                                wormDotsIndicator.gone()
                            }
                        }
                        is NetworkResult.Success -> {
                            binding?.apply {
                                shimmerImageSlider.stopShimmer()
                                shimmerImageSlider.gone()
                                viewPagerImageSlider.visible()
                                wormDotsIndicator.visible()
                            }
                            adapter.submitList(result.data.results)
                        }
                        is NetworkResult.Error -> {
                            binding?.apply {
                                shimmerImageSlider.stopShimmer()
                                shimmerImageSlider.gone()
                                viewPagerImageSlider.gone()
                                wormDotsIndicator.gone()
                            }
                        }
                        is NetworkResult.Empty -> {}
                    }
                }
            }
        binding?.viewPagerImageSlider.apply {
            this?.adapter = adapter
            binding?.wormDotsIndicator?.attachTo(this ?: this!!)
            this?.clipToPadding = false
            this?.clipChildren = false
            this?.offscreenPageLimit = 3
            this?.getChildAt(0)?.overScrollMode = RecyclerView.OVER_SCROLL_NEVER

            val compositePageTransformer = CompositePageTransformer()
            compositePageTransformer.addTransformer { page, position ->
                val r = 1 - abs(position)
                page.scaleY = 0.95f + r * 0.10f
            }
            this?.setPageTransformer(compositePageTransformer)

            this?.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    sliderHandler.removeCallbacks(sliderRunnable)
                    sliderHandler.postDelayed(sliderRunnable, 2000) // slide duration 3 seconds
                }
            })
        }
    }

    private fun setTopRatedMovies() {
        val adapter = TopRatedMoviesAdapter(object : TopRatedMoviesAdapter.OnItemClickCallback {
            override fun onItemClicked(data: ListTopRatedMovies) {
                val navigateToDetailFragment =
                    HomeFragmentDirections.actionHomeFragmentToDetailFragment()
                navigateToDetailFragment.id = data.id ?: 0
                navigateToDetailFragment.intentFrom = INTENT_FROM_MOVIE
                findNavController().navigate(navigateToDetailFragment)
            }
        })
        moviesViewModel.topRatedMovies(RxDisposer().apply { bind(lifecycle) })
            .observe(viewLifecycleOwner) { result ->
                if (result != null) {
                    when (result) {
                        is NetworkResult.Loading -> {
                            binding?.apply {
                                shimmerTopRatedMovies.startShimmer()
                                shimmerTopRatedMovies.visible()
                                rvTopRatedMovies.gone()
                            }
                        }
                        is NetworkResult.Success -> {
                            binding?.apply {
                                shimmerTopRatedMovies.stopShimmer()
                                shimmerTopRatedMovies.gone()
                                rvTopRatedMovies.visible()
                            }
                            adapter.submitList(result.data.results)
                        }
                        is NetworkResult.Error -> {
                            binding?.apply {
                                shimmerTopRatedMovies.stopShimmer()
                                shimmerTopRatedMovies.gone()
                                rvTopRatedMovies.gone()
                            }
                        }
                        is NetworkResult.Empty -> {}
                    }
                }
            }
        binding?.apply {
            rvTopRatedMovies.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            rvTopRatedMovies.adapter = adapter
            rvTopRatedMovies.setHasFixedSize(true)
        }
    }

    private fun setPopularMovies() {
        val adapter = PopularMoviesAdapter(object : PopularMoviesAdapter.OnItemClickCallback {
            override fun onItemClicked(data: ListPopularMovies) {
                val navigateToDetailFragment =
                    HomeFragmentDirections.actionHomeFragmentToDetailFragment()
                navigateToDetailFragment.id = data.id ?: 0
                navigateToDetailFragment.intentFrom = INTENT_FROM_MOVIE
                findNavController().navigate(navigateToDetailFragment)
            }
        })
        moviesViewModel.popularMovies(RxDisposer().apply { bind(lifecycle) })
            .observe(viewLifecycleOwner) { result ->
                if (result != null) {
                    when (result) {
                        is NetworkResult.Loading -> {
                            binding?.apply {
                                shimmerPopularMovies.startShimmer()
                                shimmerPopularMovies.visible()
                                rvPopularMovies.gone()
                            }
                        }
                        is NetworkResult.Success -> {
                            binding?.apply {
                                shimmerPopularMovies.stopShimmer()
                                shimmerPopularMovies.gone()
                                rvPopularMovies.visible()
                            }
                            adapter.submitList(result.data.results)
                        }
                        is NetworkResult.Error -> {
                            binding?.apply {
                                shimmerPopularMovies.stopShimmer()
                                shimmerPopularMovies.gone()
                                rvPopularMovies.gone()
                            }
                        }
                        is NetworkResult.Empty -> {}
                    }
                }
            }
        binding?.apply {
            rvPopularMovies.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            rvPopularMovies.adapter = adapter
            rvPopularMovies.setHasFixedSize(true)
        }
    }

    private fun setUpComingMovies() {
        val adapter = UpComingMoviesAdapter(object : UpComingMoviesAdapter.OnItemClickCallback {
            override fun onItemClicked(data: ListUpComingMovies) {
                val navigateToDetailFragment =
                    HomeFragmentDirections.actionHomeFragmentToDetailFragment()
                navigateToDetailFragment.id = data.id ?: 0
                navigateToDetailFragment.intentFrom = INTENT_FROM_MOVIE
                findNavController().navigate(navigateToDetailFragment)
            }
        })
        moviesViewModel.upComingMovies(RxDisposer().apply { bind(lifecycle) })
            .observe(viewLifecycleOwner) { result ->
                if (result != null) {
                    when (result) {
                        is NetworkResult.Loading -> {
                            binding?.apply {
                                shimmerUpComingMovies.startShimmer()
                                shimmerUpComingMovies.visible()
                                rvUpComingMovies.gone()
                            }
                        }
                        is NetworkResult.Success -> {
                            binding?.apply {
                                shimmerUpComingMovies.stopShimmer()
                                shimmerUpComingMovies.gone()
                                rvUpComingMovies.visible()
                            }
                            adapter.submitList(result.data.results)
                        }
                        is NetworkResult.Error -> {
                            binding?.apply {
                                shimmerUpComingMovies.stopShimmer()
                                shimmerUpComingMovies.gone()
                                rvUpComingMovies.gone()
                            }
                        }
                        is NetworkResult.Empty -> {}
                    }
                }
            }
        binding?.apply {
            rvUpComingMovies.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            rvUpComingMovies.adapter = adapter
            rvUpComingMovies.setHasFixedSize(true)
        }
    }

    override fun onPause() {
        super.onPause()
        sliderHandler.removeCallbacks(sliderRunnable)
    }

    override fun onResume() {
        super.onResume()
        sliderHandler.postDelayed(sliderRunnable, 2000)
    }
}