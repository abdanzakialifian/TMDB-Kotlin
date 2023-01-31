package com.application.zaki.movies.presentation.tvshows.view

import android.os.Handler
import android.os.Looper
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.application.zaki.movies.databinding.FragmentTvShowsBinding
import com.application.zaki.movies.domain.model.tvshows.ListAiringTodayTvShows
import com.application.zaki.movies.domain.model.tvshows.ListOnTheAirTvShows
import com.application.zaki.movies.domain.model.tvshows.ListPopularTvShows
import com.application.zaki.movies.domain.model.tvshows.ListTopRatedTvShows
import com.application.zaki.movies.presentation.base.BaseVBFragment
import com.application.zaki.movies.presentation.detail.view.DetailFragment.Companion.INTENT_FROM_TV_SHOWS
import com.application.zaki.movies.presentation.home.HomeFragmentDirections
import com.application.zaki.movies.presentation.list.view.ListFragment.Companion.INTENT_FROM_ON_THE_AIR_TV_SHOWS
import com.application.zaki.movies.presentation.list.view.ListFragment.Companion.INTENT_FROM_POPULAR_TV_SHOWS
import com.application.zaki.movies.presentation.list.view.ListFragment.Companion.INTENT_FROM_TOP_RATED_TV_SHOWS
import com.application.zaki.movies.presentation.tvshows.adapter.AiringTodayTvShowsAdapter
import com.application.zaki.movies.presentation.tvshows.adapter.OnTheAirTvShowsAdapter
import com.application.zaki.movies.presentation.tvshows.adapter.PopularTvShowsAdapter
import com.application.zaki.movies.presentation.tvshows.adapter.TopRatedTvShowsAdapter
import com.application.zaki.movies.presentation.tvshows.viewmodel.TvShowsViewModel
import com.application.zaki.movies.utils.UiState
import com.application.zaki.movies.utils.RxDisposer
import com.application.zaki.movies.utils.gone
import com.application.zaki.movies.utils.visible
import dagger.hilt.android.AndroidEntryPoint
import kotlin.math.abs

@AndroidEntryPoint
class TvShowsFragment : BaseVBFragment<FragmentTvShowsBinding>() {

    private val tvShowsViewModel by viewModels<TvShowsViewModel>()
    private val sliderHandler = Handler(Looper.getMainLooper())
    private lateinit var sliderRunnable: Runnable

    override fun getViewBinding(): FragmentTvShowsBinding =
        FragmentTvShowsBinding.inflate(layoutInflater)

    override fun initView() {
        sliderRunnable = Runnable {
            binding?.viewPagerImageSlider?.currentItem =
                binding?.viewPagerImageSlider?.currentItem?.plus(1) ?: 0
        }
        setImageSlider()
        setTopRatedTvShows()
        setPopularTvShows()
        setOnTheAirTvShows()

        binding?.apply {
            tvSeeAllTopRatedTvShows.setOnClickListener {
                val navigateToListFragment =
                    HomeFragmentDirections.actionHomeFragmentToListFragment()
                navigateToListFragment.intentFrom = INTENT_FROM_TOP_RATED_TV_SHOWS
                findNavController().navigate(navigateToListFragment)
            }

            tvSeeAllPopularTvShows.setOnClickListener {
                val navigateToListFragment =
                    HomeFragmentDirections.actionHomeFragmentToListFragment()
                navigateToListFragment.intentFrom = INTENT_FROM_POPULAR_TV_SHOWS
                findNavController().navigate(navigateToListFragment)
            }

            tvSeeAllOnTheAirTvShows.setOnClickListener {
                val navigateToListFragment =
                    HomeFragmentDirections.actionHomeFragmentToListFragment()
                navigateToListFragment.intentFrom = INTENT_FROM_ON_THE_AIR_TV_SHOWS
                findNavController().navigate(navigateToListFragment)
            }
        }
    }

    private fun setImageSlider() {
        val adapter =
            AiringTodayTvShowsAdapter(object : AiringTodayTvShowsAdapter.OnItemClickCallback {
                override fun onItemClicked(data: ListAiringTodayTvShows) {
                    val navigateToDetailFragment =
                        HomeFragmentDirections.actionHomeFragmentToDetailFragment()
                    navigateToDetailFragment.id = data.id ?: 0
                    navigateToDetailFragment.intentFrom = INTENT_FROM_TV_SHOWS
                    findNavController().navigate(navigateToDetailFragment)
                }
            })
        tvShowsViewModel.airingTodayTvShows(RxDisposer().apply { bind(lifecycle) })
            .observe(viewLifecycleOwner) { result ->
                if (result != null) {
                    when (result) {
                        is UiState.Loading -> {
                            binding?.apply {
                                shimmerImageSlider.startShimmer()
                                shimmerImageSlider.visible()
                                viewPagerImageSlider.gone()
                                wormDotsIndicator.gone()
                            }
                        }
                        is UiState.Success -> {
                            binding?.apply {
                                shimmerImageSlider.stopShimmer()
                                shimmerImageSlider.gone()
                                viewPagerImageSlider.visible()
                                wormDotsIndicator.visible()
                            }
                            adapter.submitList(result.data.results)
                        }
                        is UiState.Error -> {
                            binding?.apply {
                                shimmerImageSlider.stopShimmer()
                                shimmerImageSlider.gone()
                                viewPagerImageSlider.gone()
                                wormDotsIndicator.gone()
                            }
                        }
                        is UiState.Empty -> {}
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
                    sliderHandler.postDelayed(sliderRunnable, 3000) // slide duration 3 seconds
                }
            })
        }
    }

    private fun setTopRatedTvShows() {
        val adapter = TopRatedTvShowsAdapter(object : TopRatedTvShowsAdapter.OnItemClickCallback {
            override fun onItemClicked(data: ListTopRatedTvShows) {
                val navigateToDetailFragment =
                    HomeFragmentDirections.actionHomeFragmentToDetailFragment()
                navigateToDetailFragment.id = data.id ?: 0
                navigateToDetailFragment.intentFrom = INTENT_FROM_TV_SHOWS
                findNavController().navigate(navigateToDetailFragment)
            }
        })
        tvShowsViewModel.topRatedTvShows(RxDisposer().apply { bind(lifecycle) })
            .observe(viewLifecycleOwner) { result ->
                if (result != null) {
                    when (result) {
                        is UiState.Loading -> {
                            binding?.apply {
                                shimmerTopRatedTvShows.visible()
                                shimmerTopRatedTvShows.startShimmer()
                                rvTopRatedTvShows.gone()
                            }
                        }
                        is UiState.Success -> {
                            binding?.apply {
                                shimmerTopRatedTvShows.gone()
                                shimmerTopRatedTvShows.stopShimmer()
                                rvTopRatedTvShows.visible()
                            }
                            adapter.submitList(result.data.results)
                        }
                        is UiState.Error -> {
                            binding?.apply {
                                shimmerTopRatedTvShows.gone()
                                shimmerTopRatedTvShows.stopShimmer()
                                rvTopRatedTvShows.gone()
                            }
                        }
                        is UiState.Empty -> {}
                    }
                }
                binding?.apply {
                    rvTopRatedTvShows.layoutManager =
                        LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
                    rvTopRatedTvShows.adapter = adapter
                    rvTopRatedTvShows.setHasFixedSize(true)
                }
            }
    }

    private fun setPopularTvShows() {
        val adapter = PopularTvShowsAdapter(object : PopularTvShowsAdapter.OnItemClickCallback {
            override fun onItemClicked(data: ListPopularTvShows) {
                val navigateToDetailFragment =
                    HomeFragmentDirections.actionHomeFragmentToDetailFragment()
                navigateToDetailFragment.id = data.id ?: 0
                navigateToDetailFragment.intentFrom = INTENT_FROM_TV_SHOWS
                findNavController().navigate(navigateToDetailFragment)
            }
        })
        tvShowsViewModel.popularTvShows(RxDisposer().apply { bind(lifecycle) })
            .observe(viewLifecycleOwner) { result ->
                if (result != null) {
                    when (result) {
                        is UiState.Loading -> {
                            binding?.apply {
                                shimmerPopularTvShows.visible()
                                shimmerPopularTvShows.startShimmer()
                                rvPopularTvShows.gone()
                            }
                        }
                        is UiState.Success -> {
                            binding?.apply {
                                shimmerPopularTvShows.gone()
                                shimmerPopularTvShows.stopShimmer()
                                rvPopularTvShows.visible()
                            }
                            adapter.submitList(result.data.results)
                        }
                        is UiState.Error -> {
                            binding?.apply {
                                shimmerPopularTvShows.gone()
                                shimmerPopularTvShows.stopShimmer()
                                rvPopularTvShows.gone()
                            }
                        }
                        is UiState.Empty -> {}
                    }
                }
            }
        binding?.apply {
            rvPopularTvShows.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            rvPopularTvShows.adapter = adapter
            rvPopularTvShows.setHasFixedSize(true)
        }
    }

    private fun setOnTheAirTvShows() {
        val adapter = OnTheAirTvShowsAdapter(object : OnTheAirTvShowsAdapter.OnItemClickCallback {
            override fun onItemClicked(data: ListOnTheAirTvShows) {
                val navigateToDetailFragment =
                    HomeFragmentDirections.actionHomeFragmentToDetailFragment()
                navigateToDetailFragment.id = data.id ?: 0
                navigateToDetailFragment.intentFrom = INTENT_FROM_TV_SHOWS
                findNavController().navigate(navigateToDetailFragment)
            }
        })
        tvShowsViewModel.onTheAirTvShows(RxDisposer().apply { bind(lifecycle) })
            .observe(viewLifecycleOwner) { result ->
                if (result != null) {
                    when (result) {
                        is UiState.Loading -> {
                            binding?.apply {
                                shimmerOnTheAirTvShows.visible()
                                shimmerOnTheAirTvShows.startShimmer()
                                rvOnTheAirTvShows.gone()
                            }
                        }
                        is UiState.Success -> {
                            binding?.apply {
                                shimmerOnTheAirTvShows.gone()
                                shimmerOnTheAirTvShows.stopShimmer()
                                rvOnTheAirTvShows.visible()
                            }
                            adapter.submitList(result.data.results)
                        }
                        is UiState.Error -> {
                            binding?.apply {
                                shimmerOnTheAirTvShows.gone()
                                shimmerOnTheAirTvShows.stopShimmer()
                                rvOnTheAirTvShows.gone()
                            }
                        }
                        is UiState.Empty -> {}
                    }
                }
            }
        binding?.apply {
            rvOnTheAirTvShows.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            rvOnTheAirTvShows.adapter = adapter
            rvOnTheAirTvShows.setHasFixedSize(true)
        }
    }
}