package com.application.zaki.movies.presentation.movies.view

import android.os.Handler
import android.os.Looper
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.paging.PagingData
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
import com.application.zaki.movies.presentation.movies.adapter.MovieCategoryAdapter
import com.application.zaki.movies.presentation.movies.adapter.MovieCategoryItemAdapter
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
    lateinit var popularMoviesAdapter: MovieCategoryItemAdapter

    @Inject
    lateinit var topRatedMoviesAdapter: MovieCategoryItemAdapter

    @Inject
    lateinit var upComingMoviesAdapter: MovieCategoryItemAdapter

    @Inject
    lateinit var movieCategoryAdapter: MovieCategoryAdapter

    private val moviesViewModel by viewModels<MoviesViewModel>()
    private val sliderHandler = Handler(Looper.getMainLooper())
    private lateinit var sliderRunnable: Runnable
    private val moviesCategory = ArrayList<MoviesCategoryItem>()

    override fun getViewBinding(): FragmentMoviesBinding =
        FragmentMoviesBinding.inflate(layoutInflater)

    override fun initView() {
        sliderRunnable = Runnable {
            binding?.viewPagerImageSlider?.apply {
                currentItem += 1
            }
        }
        setMoviesSlider()
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

        topRatedMoviesAdapter.setOnItemClickCallback(object :
            MovieCategoryItemAdapter.OnItemClickCallback {
            override fun onItemClicked(data: ListMovies?) {
                navigateToDetailPage(data?.id ?: 0)
            }
        })

        popularMoviesAdapter.setOnItemClickCallback(object :
            MovieCategoryItemAdapter.OnItemClickCallback {
            override fun onItemClicked(data: ListMovies?) {
                navigateToDetailPage(data?.id ?: 0)
            }
        })

        upComingMoviesAdapter.setOnItemClickCallback(object :
            MovieCategoryItemAdapter.OnItemClickCallback {
            override fun onItemClicked(data: ListMovies?) {
                navigateToDetailPage(data?.id ?: 0)
            }
        })
        movieCategoryAdapter.setOnSeeAllClickCallback(object :
            MovieCategoryAdapter.OnSeeAllClickCallback {
            override fun onSeeAllClicked(movie: Movie) {
                navigateToListPage(movie)
            }
        })
    }

    private fun setMoviesSlider() {
        moviesViewModel.getMovies(
            movie = Movie.NOW_PLAYING_MOVIES,
            genre = Genre.MOVIES,
            page = Page.MORE_THAN_ONE,
            rxDisposer = RxDisposer().apply { bind(lifecycle) }
        ).observe(viewLifecycleOwner) { result ->
            nowPlayingMoviesAdapter.submitData(lifecycle, result)
            nowPlayingMoviesAdapter.addLoadStateListener { loadState ->
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
        }
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

    private fun setMovies() {
        moviesViewModel.getMovies(
            movie = Movie.TOP_RATED_MOVIES,
            genre = Genre.MOVIES,
            page = Page.ONE,
            rxDisposer = RxDisposer().apply { bind(lifecycle) }
        ).observe(viewLifecycleOwner) { result ->
            val data = MoviesCategoryItem(
                categoryTitle = resources.getString(R.string.top_rated_movies),
                categories = result
            )
            moviesCategory.add(data)

            movieCategoryAdapter.submitList(moviesCategory)
            movieCategoryAdapter.setLifecycle(lifecycle)
            binding?.rvMovies?.apply {
                adapter = movieCategoryAdapter
                setHasFixedSize(true)
            }
        }

        moviesViewModel.getMovies(
            movie = Movie.POPULAR_MOVIES,
            genre = Genre.MOVIES,
            page = Page.ONE,
            rxDisposer = RxDisposer().apply { bind(lifecycle) }
        ).observe(viewLifecycleOwner) { result ->
            val data = MoviesCategoryItem(
                categoryTitle = resources.getString(R.string.popular_movies),
                categories = result
            )
            moviesCategory.add(data)

            movieCategoryAdapter.submitList(moviesCategory)
            movieCategoryAdapter.setLifecycle(lifecycle)
            binding?.rvMovies?.apply {
                adapter = movieCategoryAdapter
                setHasFixedSize(true)
            }
        }

        moviesViewModel.getMovies(
            movie = Movie.UP_COMING_MOVIES,
            genre = Genre.MOVIES,
            page = Page.ONE,
            rxDisposer = RxDisposer().apply { bind(lifecycle) }
        ).observe(viewLifecycleOwner) { result ->
            val data = MoviesCategoryItem(
                categoryTitle = resources.getString(R.string.up_coming_movies),
                categories = result
            )
            moviesCategory.add(data)

            movieCategoryAdapter.submitList(moviesCategory)
            movieCategoryAdapter.setLifecycle(lifecycle)
            binding?.rvMovies?.apply {
                adapter = movieCategoryAdapter
                setHasFixedSize(true)
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

    private fun navigateToListPage(movie: Movie) {
        val navigateToListFragment =
            HomeFragmentDirections.actionHomeFragmentToListFragment()
        navigateToListFragment.intentFrom = movie
        findNavController().navigate(navigateToListFragment)
    }

    override fun onPause() {
        sliderHandler.removeCallbacks(sliderRunnable)
        super.onPause()
    }

    override fun onResume() {
        super.onResume()
        sliderHandler.postDelayed(sliderRunnable, 2000)
    }
}