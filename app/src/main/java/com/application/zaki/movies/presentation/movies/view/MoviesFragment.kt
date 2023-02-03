package com.application.zaki.movies.presentation.movies.view

import android.os.Handler
import android.os.Looper
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.application.zaki.movies.data.source.remote.RemoteDataSource
import com.application.zaki.movies.data.source.remote.paging.movies.PopularMoviesRxPagingSource
import com.application.zaki.movies.data.source.remote.paging.movies.TopRatedMoviesRxPagingSource
import com.application.zaki.movies.data.source.remote.paging.movies.UpComingMoviesRxPagingSource
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
import com.application.zaki.movies.utils.RxDisposer
import com.application.zaki.movies.utils.UiState
import com.application.zaki.movies.utils.gone
import com.application.zaki.movies.utils.visible
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlin.math.abs

@AndroidEntryPoint
class MoviesFragment : BaseVBFragment<FragmentMoviesBinding>() {

    @Inject
    lateinit var popularMoviesAdapter: PopularMoviesAdapter

    @Inject
    lateinit var nowPlayingMoviesAdapter: NowPlayingMoviesAdapter

    @Inject
    lateinit var topRatedMoviesAdapter: TopRatedMoviesAdapter

    @Inject
    lateinit var upComingMoviesAdapter: UpComingMoviesAdapter

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
                navigateToListPage(INTENT_FROM_TOP_RATED_MOVIES)
            }

            tvSeeAllPopularMovies.setOnClickListener {
                navigateToListPage(INTENT_FROM_POPULAR_MOVIES)
            }
            tvSeeAllUpComingMovies.setOnClickListener {
                navigateToListPage(INTENT_FROM_UP_COMING_MOVIES)
            }
        }
    }

    private fun setImageSlider() {
        nowPlayingMoviesAdapter.setOnItemClickCallback(object :
            NowPlayingMoviesAdapter.OnItemClickCallback {
            override fun onItemClicked(data: ListNowPlayingMovies) {
                navigateToDetailPage(data.id ?: 0)
            }
        })
        moviesViewModel.nowPlayingMovies(RxDisposer().apply { bind(lifecycle) })
            .observe(viewLifecycleOwner) { result ->
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
                        nowPlayingMoviesAdapter.submitList(result.data.results)
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
        binding?.viewPagerImageSlider.apply {
            this?.adapter = nowPlayingMoviesAdapter
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
        topRatedMoviesAdapter.setOnItemClickCallback(object :
            TopRatedMoviesAdapter.OnItemClickCallback {
            override fun onItemClicked(data: ListTopRatedMovies?) {
                navigateToDetailPage(data?.id ?: 0)
            }
        })
        moviesViewModel.topRatedMoviesPaging(
            RxDisposer().apply { bind(lifecycle) },
            RemoteDataSource.MOVIES,
            TopRatedMoviesRxPagingSource.ONE
        ).observe(viewLifecycleOwner) { result ->
            topRatedMoviesAdapter.submitData(lifecycle, result)
            binding?.apply {
                rvTopRatedMovies.adapter = topRatedMoviesAdapter
                rvTopRatedMovies.setHasFixedSize(true)
            }

            topRatedMoviesAdapter.addLoadStateListener { loadState ->
                when (loadState.refresh) {
                    is LoadState.Loading -> {
                        binding?.apply {
                            shimmerTopRatedMovies.startShimmer()
                            shimmerTopRatedMovies.visible()
                            rvTopRatedMovies.gone()
                        }
                    }
                    is LoadState.NotLoading -> {
                        binding?.apply {
                            shimmerTopRatedMovies.stopShimmer()
                            shimmerTopRatedMovies.gone()
                            rvTopRatedMovies.visible()
                        }
                    }
                    is LoadState.Error -> {
                        binding?.apply {
                            shimmerTopRatedMovies.stopShimmer()
                            shimmerTopRatedMovies.gone()
                            rvTopRatedMovies.gone()
                        }
                    }
                }
            }
        }
    }

    private fun setPopularMovies() {
        popularMoviesAdapter.setOnItemClickCallback(object :
            PopularMoviesAdapter.OnItemClickCallback {
            override fun onItemClicked(data: ListPopularMovies?) {
                navigateToDetailPage(data?.id ?: 0)
            }
        })
        moviesViewModel.popularMoviesPaging(
            RxDisposer().apply { bind(lifecycle) },
            RemoteDataSource.MOVIES,
            PopularMoviesRxPagingSource.ONE
        ).observe(viewLifecycleOwner) { result ->
            popularMoviesAdapter.submitData(lifecycle, result)
            binding?.apply {
                rvPopularMovies.adapter = popularMoviesAdapter
                rvPopularMovies.setHasFixedSize(true)
            }

            popularMoviesAdapter.addLoadStateListener { loadState ->
                when (loadState.refresh) {
                    is LoadState.Loading -> {
                        binding?.apply {
                            shimmerPopularMovies.startShimmer()
                            shimmerPopularMovies.visible()
                            rvPopularMovies.gone()
                        }
                    }
                    is LoadState.NotLoading -> {
                        binding?.apply {
                            shimmerPopularMovies.stopShimmer()
                            shimmerPopularMovies.gone()
                            rvPopularMovies.visible()
                        }
                    }
                    is LoadState.Error -> {
                        binding?.apply {
                            shimmerPopularMovies.stopShimmer()
                            shimmerPopularMovies.gone()
                            rvPopularMovies.gone()
                        }
                    }
                }
            }
        }
    }

    private fun setUpComingMovies() {
        upComingMoviesAdapter.setOnItemClickCallback(object :
            UpComingMoviesAdapter.OnItemClickCallback {
            override fun onItemClicked(data: ListUpComingMovies?) {
                navigateToDetailPage(data?.id ?: 0)
            }
        })
        moviesViewModel.upComingMoviesPaging(
            RxDisposer().apply { bind(lifecycle) },
            RemoteDataSource.MOVIES,
            UpComingMoviesRxPagingSource.ONE
        ).observe(viewLifecycleOwner) { result ->
            upComingMoviesAdapter.submitData(lifecycle, result)
            binding?.apply {
                rvUpComingMovies.adapter = upComingMoviesAdapter
                rvUpComingMovies.setHasFixedSize(true)
            }
        }

        upComingMoviesAdapter.addLoadStateListener { loadState ->
            when (loadState.refresh) {
                is LoadState.Loading -> {
                    binding?.apply {
                        shimmerUpComingMovies.startShimmer()
                        shimmerUpComingMovies.visible()
                        rvUpComingMovies.gone()
                    }
                }
                is LoadState.NotLoading -> {
                    binding?.apply {
                        shimmerUpComingMovies.stopShimmer()
                        shimmerUpComingMovies.gone()
                        rvUpComingMovies.visible()
                    }
                }
                is LoadState.Error -> {
                    binding?.apply {
                        shimmerUpComingMovies.stopShimmer()
                        shimmerUpComingMovies.gone()
                        rvUpComingMovies.gone()
                    }
                }
            }
        }
    }

    private fun navigateToDetailPage(id: Int) {
        val navigateToDetailFragment =
            HomeFragmentDirections.actionHomeFragmentToDetailFragment()
        navigateToDetailFragment.id = id
        navigateToDetailFragment.intentFrom = INTENT_FROM_MOVIE
        findNavController().navigate(navigateToDetailFragment)
    }

    private fun navigateToListPage(intentFrom: String) {
        val navigateToListFragment =
            HomeFragmentDirections.actionHomeFragmentToListFragment()
        navigateToListFragment.intentFrom = intentFrom
        findNavController().navigate(navigateToListFragment)
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