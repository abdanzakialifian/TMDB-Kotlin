package com.application.zaki.movies.presentation.tvshows.view

import android.os.Handler
import android.os.Looper
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.application.zaki.movies.data.source.remote.paging.tvshows.OnTheAirTvShowsRxPagingSource
import com.application.zaki.movies.data.source.remote.paging.tvshows.PopularTvShowsRxPagingSource
import com.application.zaki.movies.data.source.remote.paging.tvshows.TopRatedTvShowsRxPagingSource
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
import com.application.zaki.movies.utils.*
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlin.math.abs

@AndroidEntryPoint
class TvShowsFragment : BaseVBFragment<FragmentTvShowsBinding>() {

    @Inject
    lateinit var airingTodayTvShowsAdapter: AiringTodayTvShowsAdapter

    @Inject
    lateinit var onTheAirTvShowsAdapter: OnTheAirTvShowsAdapter

    @Inject
    lateinit var popularTvShowsAdapter: PopularTvShowsAdapter

    @Inject
    lateinit var topRatedTvShowsAdapter: TopRatedTvShowsAdapter

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
                navigateToListPage(INTENT_FROM_TOP_RATED_TV_SHOWS)
            }

            tvSeeAllPopularTvShows.setOnClickListener {
                navigateToListPage(INTENT_FROM_POPULAR_TV_SHOWS)
            }

            tvSeeAllOnTheAirTvShows.setOnClickListener {
                navigateToListPage(INTENT_FROM_ON_THE_AIR_TV_SHOWS)
            }
        }
    }

    private fun setImageSlider() {
        airingTodayTvShowsAdapter.setOnItemClickCallback(object :
            AiringTodayTvShowsAdapter.OnItemClickCallback {
            override fun onItemClicked(data: ListAiringTodayTvShows) {
                navigateToDetailPage(data.id ?: 0)
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
                            airingTodayTvShowsAdapter.submitList(result.data.results)
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
            this?.adapter = airingTodayTvShowsAdapter
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
        topRatedTvShowsAdapter.setOnItemClickCallback(object :
            TopRatedTvShowsAdapter.OnItemClickCallback {
            override fun onItemClicked(data: ListTopRatedTvShows?) {
                navigateToDetailPage(data?.id ?: 0)
            }
        })
        tvShowsViewModel.topRatedTvShowsPaging(
            RxDisposer().apply { bind(lifecycle) },
            Genre.TV_SHOWS,
            TopRatedTvShowsRxPagingSource.ONE
        ).observe(viewLifecycleOwner) { result ->
            topRatedTvShowsAdapter.submitData(lifecycle, result)
            binding?.apply {
                rvTopRatedTvShows.adapter = topRatedTvShowsAdapter
                rvTopRatedTvShows.setHasFixedSize(true)
            }

            topRatedTvShowsAdapter.addLoadStateListener { loadState ->
                when (loadState.refresh) {
                    is LoadState.Loading -> {
                        binding?.apply {
                            shimmerTopRatedTvShows.visible()
                            shimmerTopRatedTvShows.startShimmer()
                            rvTopRatedTvShows.gone()
                        }
                    }
                    is LoadState.NotLoading -> {
                        binding?.apply {
                            shimmerTopRatedTvShows.gone()
                            shimmerTopRatedTvShows.stopShimmer()
                            rvTopRatedTvShows.visible()
                        }
                    }
                    is LoadState.Error -> {
                        binding?.apply {
                            shimmerTopRatedTvShows.gone()
                            shimmerTopRatedTvShows.stopShimmer()
                            rvTopRatedTvShows.gone()
                        }
                    }
                }
            }
        }
    }

    private fun setPopularTvShows() {
        popularTvShowsAdapter.setOnItemClickCallback(object :
            PopularTvShowsAdapter.OnItemClickCallback {
            override fun onItemClicked(data: ListPopularTvShows?) {
                navigateToDetailPage(data?.id ?: 0)
            }
        })
        tvShowsViewModel.popularTvShowsPaging(
            RxDisposer().apply { bind(lifecycle) },
            Genre.TV_SHOWS,
            PopularTvShowsRxPagingSource.ONE
        ).observe(viewLifecycleOwner) { result ->
            popularTvShowsAdapter.submitData(lifecycle, result)
            binding?.apply {
                rvPopularTvShows.adapter = popularTvShowsAdapter
                rvPopularTvShows.setHasFixedSize(true)
            }

            popularTvShowsAdapter.addLoadStateListener { loadState ->
                when (loadState.refresh) {
                    is LoadState.Loading -> {
                        binding?.apply {
                            shimmerPopularTvShows.visible()
                            shimmerPopularTvShows.startShimmer()
                            rvPopularTvShows.gone()
                        }
                    }
                    is LoadState.NotLoading -> {
                        binding?.apply {
                            shimmerPopularTvShows.gone()
                            shimmerPopularTvShows.stopShimmer()
                            rvPopularTvShows.visible()
                        }
                    }
                    is LoadState.Error -> {
                        binding?.apply {
                            shimmerPopularTvShows.gone()
                            shimmerPopularTvShows.stopShimmer()
                            rvPopularTvShows.gone()
                        }
                    }
                }
            }
        }
    }

    private fun setOnTheAirTvShows() {
        onTheAirTvShowsAdapter.setOnItemClickCallback(object :
            OnTheAirTvShowsAdapter.OnItemClickCallback {
            override fun onItemClicked(data: ListOnTheAirTvShows?) {
                navigateToDetailPage(data?.id ?: 0)
            }
        })
        tvShowsViewModel.onTheAirTvShowsPaging(
            RxDisposer().apply { bind(lifecycle) },
            Genre.TV_SHOWS,
            OnTheAirTvShowsRxPagingSource.ONE
        ).observe(viewLifecycleOwner) { result ->
            onTheAirTvShowsAdapter.submitData(lifecycle, result)
            binding?.apply {
                rvOnTheAirTvShows.adapter = onTheAirTvShowsAdapter
                rvOnTheAirTvShows.setHasFixedSize(true)
            }

            onTheAirTvShowsAdapter.addLoadStateListener { loadState ->
                when (loadState.refresh) {
                    is LoadState.Loading -> {
                        binding?.apply {
                            shimmerOnTheAirTvShows.visible()
                            shimmerOnTheAirTvShows.startShimmer()
                            rvOnTheAirTvShows.gone()
                        }
                    }
                    is LoadState.NotLoading -> {
                        binding?.apply {
                            shimmerOnTheAirTvShows.gone()
                            shimmerOnTheAirTvShows.stopShimmer()
                            rvOnTheAirTvShows.visible()
                        }
                    }
                    is LoadState.Error -> {
                        binding?.apply {
                            shimmerOnTheAirTvShows.gone()
                            shimmerOnTheAirTvShows.stopShimmer()
                            rvOnTheAirTvShows.gone()
                        }
                    }
                }
            }
        }
    }

    private fun navigateToDetailPage(id: Int) {
        val navigateToDetailFragment =
            HomeFragmentDirections.actionHomeFragmentToDetailFragment()
        navigateToDetailFragment.id = id
        navigateToDetailFragment.intentFrom = INTENT_FROM_TV_SHOWS
        findNavController().navigate(navigateToDetailFragment)
    }

    private fun navigateToListPage(intentFrom: String) {
        val navigateToListFragment =
            HomeFragmentDirections.actionHomeFragmentToListFragment()
//        navigateToListFragment.intentFrom = intentFrom
        findNavController().navigate(navigateToListFragment)
    }
}