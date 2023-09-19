package com.application.zaki.movies.presentation.movies.view

import android.os.Handler
import android.os.Looper
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.application.zaki.movies.R
import com.application.zaki.movies.databinding.FragmentMoviesBinding
import com.application.zaki.movies.domain.model.movies.ListMovies
import com.application.zaki.movies.domain.model.movies.MoviesCategoryItem
import com.application.zaki.movies.presentation.base.BaseVBFragment
import com.application.zaki.movies.presentation.detail.view.DetailFragment.Companion.INTENT_FROM_MOVIE
import com.application.zaki.movies.presentation.home.HomeFragmentDirections
import com.application.zaki.movies.presentation.movies.adapter.MovieAdapter
import com.application.zaki.movies.presentation.movies.adapter.NowPlayingMoviesAdapter
import com.application.zaki.movies.presentation.movies.viewmodel.MoviesViewModel
import com.application.zaki.movies.utils.*
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlin.math.abs

@AndroidEntryPoint
class MoviesFragment : BaseVBFragment<FragmentMoviesBinding>() {

    @Inject
    lateinit var nowPlayingMoviesAdapter: NowPlayingMoviesAdapter

    @Inject
    lateinit var movieAdapter: MovieAdapter

    private val moviesViewModel by viewModels<MoviesViewModel>()

    private val movieCategories = ArrayList<MoviesCategoryItem>()

    private val sliderHandler = Handler(Looper.getMainLooper())
    private lateinit var sliderRunnable: Runnable

    override fun getViewBinding(): FragmentMoviesBinding =
        FragmentMoviesBinding.inflate(layoutInflater)

    override fun initView() {
        sliderRunnable = Runnable {
            binding?.viewPagerImageSlider?.apply {
                currentItem += 1
            }
        }
        setMovies()
        eventListener()
    }

    private fun eventListener() {
        nowPlayingMoviesAdapter.setOnItemClickCallback(object :
            NowPlayingMoviesAdapter.OnItemClickCallback {
            override fun onItemClicked(data: ListMovies?) {
                navigateToDetailPage(data?.id ?: 0)
            }
        })

        movieAdapter.setOnSeeAllClickCallback(object : MovieAdapter.OnEventClickCallback {
            override fun onSeeAllClicked(movie: Movie) {
                navigateToListPage(movie)
            }

            override fun onItemClicked(data: ListMovies?) {
                navigateToDetailPage(data?.id ?: 0)
            }
        })
    }

    private fun setMovies() {
        moviesViewModel.getMovies(
            movie = Movie.NOW_PLAYING_MOVIES,
            category = Category.MOVIES,
            page = Page.MORE_THAN_ONE,
            rxDisposer = RxDisposer().apply { bind(lifecycle) }
        ).observe(viewLifecycleOwner) { result ->
            nowPlayingMoviesAdapter.submitData(lifecycle, result)
            nowPlayingMoviesAdapter.addLoadStateListener { loadState ->
                setLoadStatePagingSlider(loadState)
            }
            configureImageSlider()
        }

        moviesViewModel.getMovies(
            movie = Movie.TOP_RATED_MOVIES,
            category = Category.MOVIES,
            page = Page.ONE,
            rxDisposer = RxDisposer().apply { bind(lifecycle) }
        ).observe(viewLifecycleOwner) { result ->
            val data = MoviesCategoryItem(
                categoryTitle = resources.getString(R.string.top_rated_movies),
                categories = result
            )
            movieCategories.add(data)
            setMoviesAdapter()
        }

        moviesViewModel.getMovies(
            movie = Movie.POPULAR_MOVIES,
            category = Category.MOVIES,
            page = Page.ONE,
            rxDisposer = RxDisposer().apply { bind(lifecycle) }
        ).observe(viewLifecycleOwner) { result ->
            val data = MoviesCategoryItem(
                categoryTitle = resources.getString(R.string.popular_movies),
                categories = result
            )
            movieCategories.add(data)
            setMoviesAdapter()
        }

        moviesViewModel.getMovies(
            movie = Movie.UP_COMING_MOVIES,
            category = Category.MOVIES,
            page = Page.ONE,
            rxDisposer = RxDisposer().apply { bind(lifecycle) }
        ).observe(viewLifecycleOwner) { result ->
            val data = MoviesCategoryItem(
                categoryTitle = resources.getString(R.string.up_coming_movies),
                categories = result
            )
            movieCategories.add(data)
            setMoviesAdapter()
        }
    }

    private fun configureImageSlider() {
        binding?.viewPagerImageSlider?.apply {
            adapter = nowPlayingMoviesAdapter
            clipToPadding = false
            clipChildren = false
            offscreenPageLimit = 3
            getChildAt(0)?.overScrollMode = RecyclerView.OVER_SCROLL_NEVER

            val compositePageTransformer = CompositePageTransformer()
            compositePageTransformer.addTransformer { page, position ->
                val r = 1 - abs(position)
                page.scaleY = 0.95f + r * 0.10f
            }
            setPageTransformer(compositePageTransformer)

            registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    sliderHandler.removeCallbacks(sliderRunnable)
                    sliderHandler.postDelayed(sliderRunnable, 2000) // slide duration 3 seconds
                }
            })
        }
    }

    private fun setLoadStatePagingSlider(loadState: CombinedLoadStates) {
        when (loadState.refresh) {
            is LoadState.NotLoading -> binding?.apply {
                shimmerImageSlider.stopShimmer()
                shimmerImageSlider.gone()
                viewPagerImageSlider.visible()
            }
            LoadState.Loading -> binding?.apply {
                shimmerImageSlider.startShimmer()
                shimmerImageSlider.visible()
                viewPagerImageSlider.gone()
            }
            is LoadState.Error -> binding?.apply {
                shimmerImageSlider.stopShimmer()
                shimmerImageSlider.gone()
                viewPagerImageSlider.gone()
            }
        }
    }

    private fun setMoviesAdapter() {
        movieAdapter.submitList(movieCategories)
        movieAdapter.setLifecycle(lifecycle)
        binding?.rvMovies?.adapter = movieAdapter
        binding?.rvMovies?.setHasFixedSize(true)
    }

    private fun navigateToDetailPage(id: Int) {
        val navigateToDetailFragment =
            HomeFragmentDirections.actionHomeFragmentToDetailFragment()
        navigateToDetailFragment.id = id
        navigateToDetailFragment.intentFrom = INTENT_FROM_MOVIE
        findNavController().navigate(navigateToDetailFragment)
    }

    private fun navigateToListPage(movie: Movie) {
        val navigateToListFragment =
            HomeFragmentDirections.actionHomeFragmentToListFragment()
        navigateToListFragment.intentFrom = movie
        findNavController().navigate(navigateToListFragment)
    }

    override fun onResume() {
        super.onResume()
        sliderHandler.postDelayed(sliderRunnable, 2000)
    }

    override fun onPause() {
        sliderHandler.removeCallbacks(sliderRunnable)
        super.onPause()
    }

    override fun onDestroyView() {
        movieCategories.clear()
        super.onDestroyView()
    }
}