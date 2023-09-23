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
import com.application.zaki.movies.domain.model.CategoryItem
import com.application.zaki.movies.domain.model.MovieTvShow
import com.application.zaki.movies.presentation.adapter.MovieTvShowAdapter
import com.application.zaki.movies.presentation.adapter.MovieTvShowSliderAdapter
import com.application.zaki.movies.presentation.base.BaseVBFragment
import com.application.zaki.movies.presentation.detail.view.DetailFragment.Companion.INTENT_FROM_MOVIE
import com.application.zaki.movies.presentation.home.HomeFragmentDirections
import com.application.zaki.movies.presentation.movies.viewmodel.MoviesViewModel
import com.application.zaki.movies.utils.Category
import com.application.zaki.movies.utils.Movie
import com.application.zaki.movies.utils.Page
import com.application.zaki.movies.utils.RxDisposer
import com.application.zaki.movies.utils.gone
import com.application.zaki.movies.utils.visible
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlin.math.abs

@AndroidEntryPoint
class MoviesFragment : BaseVBFragment<FragmentMoviesBinding>(),
    MovieTvShowSliderAdapter.OnItemClickCallback, MovieTvShowAdapter.OnEventClickCallback {

    @Inject
    lateinit var movieTvShowSliderAdapter: MovieTvShowSliderAdapter

    @Inject
    lateinit var movieTvShowAdapter: MovieTvShowAdapter

    private lateinit var sliderRunnable: Runnable

    private val moviesViewModel by viewModels<MoviesViewModel>()

    private val categoryItems = mutableListOf(
        CategoryItem(), CategoryItem(), CategoryItem()
    )

    private val sliderHandler = Handler(Looper.getMainLooper())

    override fun getViewBinding(): FragmentMoviesBinding =
        FragmentMoviesBinding.inflate(layoutInflater)

    override fun initView() {
        sliderRunnable = Runnable {
            binding?.viewPagerImageSlider?.apply {
                currentItem += 1
            }
        }
        setAllMovies()
        eventListeners()
    }

    private fun eventListeners() {
        movieTvShowSliderAdapter.setOnItemClickCallback(this)
        movieTvShowAdapter.setOnEventClickCallback(this)
    }

    private fun setAllMovies() {
        setAllMoviesAdapter()
        moviesViewModel.getListAllMovies(nowPlayingMovie = Movie.NOW_PLAYING_MOVIES,
            topRatedMovie = Movie.TOP_RATED_MOVIES,
            popularMovie = Movie.POPULAR_MOVIES,
            upComingMovie = Movie.UP_COMING_MOVIES,
            category = Category.MOVIES,
            page = Page.ONE,
            rxDisposer = RxDisposer().apply { bind(lifecycle) })
            .observe(viewLifecycleOwner) { result ->
                result.forEachIndexed { index, pairMovie ->
                    val movie = pairMovie.first
                    val moviePaging = pairMovie.second

                    if (movie == Movie.NOW_PLAYING_MOVIES) {
                        configureImageSlider()
                        movieTvShowSliderAdapter.submitData(lifecycle, moviePaging)
                        movieTvShowSliderAdapter.addLoadStateListener { loadState ->
                            setLoadStatePagingSlider(loadState)
                        }
                    } else {
                        val title = when (movie) {
                            Movie.TOP_RATED_MOVIES -> resources.getString(R.string.top_rated_movies)
                            Movie.POPULAR_MOVIES -> resources.getString(R.string.popular_movies)
                            else -> resources.getString(R.string.up_coming_movies)
                        }

                        val data = CategoryItem(
                            categoryId = index,
                            categoryTitle = title,
                            categories = moviePaging,
                            category = Category.MOVIES
                        )
                        // remove first dummy model before add to list
                        categoryItems.removeAt(0)
                        categoryItems.add(data)
                    }
                }
                setAllMoviesAdapter()
            }
    }

    private fun configureImageSlider() {
        binding?.viewPagerImageSlider?.apply {
            adapter = movieTvShowSliderAdapter
            clipToPadding = false
            clipChildren = false
            offscreenPageLimit = 3
            getChildAt(0)?.overScrollMode = RecyclerView.OVER_SCROLL_NEVER

            val compositePageTransformer = CompositePageTransformer()
            compositePageTransformer.addTransformer { page, position ->
                val r = 1 - abs(position)
                page.scaleY = 0.95f + r * 0.1f
            }
            setPageTransformer(compositePageTransformer)

            registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    if (this@MoviesFragment::sliderRunnable.isInitialized) {
                        sliderHandler.removeCallbacks(sliderRunnable)
                        sliderHandler.postDelayed(sliderRunnable, 2000L) // slide duration 2 seconds
                    }
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

    private fun setAllMoviesAdapter() {
        movieTvShowAdapter.submitList(categoryItems)
        binding?.apply {
            rvMovies.adapter = movieTvShowAdapter
            rvMovies.setHasFixedSize(true)
        }
    }

    private fun navigateToDetailPage(id: Int) {
        val navigateToDetailFragment = HomeFragmentDirections.actionHomeFragmentToDetailFragment()
        navigateToDetailFragment.id = id
        navigateToDetailFragment.intentFrom = INTENT_FROM_MOVIE
        findNavController().navigate(navigateToDetailFragment)
    }

    private fun navigateToListPage(category: Category) {
        val navigateToListFragment =
            HomeFragmentDirections.actionHomeFragmentToListFragment(category.name)
        findNavController().navigate(navigateToListFragment)
    }

    override fun onSeeAllClicked(category: Category?) {
        navigateToListPage(category ?: Category.MOVIES)
    }

    override fun onItemClicked(data: MovieTvShow?) {
        navigateToDetailPage(data?.id ?: 0)
    }

    override fun onResume() {
        super.onResume()
        if (this::sliderRunnable.isInitialized) {
            sliderHandler.postDelayed(sliderRunnable, 2000L)
        }
    }

    override fun onPause() {
        if (this::sliderRunnable.isInitialized) {
            sliderHandler.removeCallbacks(sliderRunnable)
        }
        super.onPause()
    }

    override fun onDestroyView() {
        categoryItems.clear()
        super.onDestroyView()
    }
}