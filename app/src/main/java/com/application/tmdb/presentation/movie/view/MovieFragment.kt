package com.application.tmdb.presentation.movie.view

import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.application.tmdb.R
import com.application.tmdb.databinding.FragmentMovieBinding
import com.application.tmdb.core.domain.model.CategoryModel
import com.application.tmdb.core.domain.model.MovieTvShowModel
import com.application.tmdb.presentation.adapter.MovieTvShowAdapter
import com.application.tmdb.presentation.adapter.MovieTvShowSliderPagingAdapter
import com.application.tmdb.presentation.base.BaseVBFragment
import com.application.tmdb.presentation.movie.viewmodel.MovieViewModel
import com.application.tmdb.presentation.movietvshow.adapter.MovieTvShowPagingAdapter
import com.application.tmdb.common.Category
import com.application.tmdb.common.Movie
import com.application.tmdb.common.Page
import com.application.tmdb.common.RxDisposer
import com.application.tmdb.common.TvShow
import com.application.tmdb.common.gone
import com.application.tmdb.common.hideKeyboard
import com.application.tmdb.common.visible
import com.mancj.materialsearchbar.MaterialSearchBar
import dagger.hilt.android.AndroidEntryPoint
import kotlin.math.abs

@AndroidEntryPoint
class MovieFragment : BaseVBFragment<FragmentMovieBinding>(),
    MovieTvShowSliderPagingAdapter.OnItemClickCallback, MovieTvShowAdapter.OnEventClickCallback,
    MaterialSearchBar.OnSearchActionListener, MovieTvShowPagingAdapter.OnItemClickCallback,
    TextWatcher {

    private val movieTvShowSliderPagingAdapter by lazy { MovieTvShowSliderPagingAdapter() }

    private val movieTvShowAdapter by lazy { MovieTvShowAdapter() }

    private val movieTvShowPagingAdapter by lazy { MovieTvShowPagingAdapter() }

    private lateinit var sliderRunnable: Runnable

    private val movieViewModel by viewModels<MovieViewModel>()

    private val categoryModels = mutableListOf<CategoryModel>()

    private val sliderHandler = Handler(Looper.getMainLooper())

    override fun getViewBinding(): FragmentMovieBinding =
        FragmentMovieBinding.inflate(layoutInflater)

    override fun initView() {
        sliderRunnable = Runnable {
            binding?.viewPagerImageSlider?.apply {
                currentItem += 1
            }
        }

        if (movieViewModel.listMoviesPaging.value == null) {
            movieViewModel.getListAllMovies(
                nowPlayingMovie = com.application.tmdb.common.Movie.NOW_PLAYING_MOVIES,
                topRatedMovie = com.application.tmdb.common.Movie.TOP_RATED_MOVIES,
                popularMovie = com.application.tmdb.common.Movie.POPULAR_MOVIES,
                upComingMovie = com.application.tmdb.common.Movie.UP_COMING_MOVIES,
                page = com.application.tmdb.common.Page.ONE,
                query = null,
                movieId = null,
                rxDisposer = com.application.tmdb.common.RxDisposer().apply { bind(viewLifecycleOwner.lifecycle) }
            )
        }

        observeData()

        eventListeners()
    }

    private fun eventListeners() {
        movieTvShowSliderPagingAdapter.setOnItemClickCallback(this)
        movieTvShowAdapter.setOnEventClickCallback(this)
        binding?.apply {
            searchBar.setOnSearchActionListener(this@MovieFragment)
            searchBar.addTextChangeListener(this@MovieFragment)
        }
    }

    private fun observeData() {
        movieViewModel.listMoviesPaging.observe(viewLifecycleOwner) { result ->
            result.forEachIndexed { index, pairMovie ->
                val movie = pairMovie.first
                val moviePaging = pairMovie.second

                if (movie == com.application.tmdb.common.Movie.NOW_PLAYING_MOVIES) {
                    movieTvShowSliderPagingAdapter.submitData(viewLifecycleOwner.lifecycle, moviePaging)
                    configureImageSlider()
                    movieTvShowSliderPagingAdapter.addLoadStateListener { loadState ->
                        setLoadStatePagingSlider(loadState)
                    }
                } else {
                    val title = when (movie) {
                        com.application.tmdb.common.Movie.TOP_RATED_MOVIES -> resources.getString(R.string.top_rated_movies)
                        com.application.tmdb.common.Movie.POPULAR_MOVIES -> resources.getString(R.string.popular_movies)
                        else -> resources.getString(R.string.up_coming_movies)
                    }

                    val data = CategoryModel(
                        categoryId = index,
                        categoryTitle = title,
                        categories = moviePaging,
                        category = com.application.tmdb.common.Category.MOVIES,
                        movie = movie
                    )
                    categoryModels.add(data)
                }
            }
            setAllMoviesAdapter()
        }

        movieViewModel.listSearchMoviesPaging.observe(viewLifecycleOwner) { result ->
            movieTvShowPagingAdapter.submitData(viewLifecycleOwner.lifecycle, result)
            movieTvShowPagingAdapter.setOnItemClickCallback(this)
            binding?.apply {
                rvSearchMovies.adapter = movieTvShowPagingAdapter
                rvSearchMovies.setHasFixedSize(true)
            }
            movieTvShowPagingAdapter.addLoadStateListener { loadState ->
                setLoadStatePaging(loadState)
            }
        }

        movieViewModel.isSearchStateChanged.observe(viewLifecycleOwner) { isSearchStateChanged ->
            binding?.apply {
                if (isSearchStateChanged) {
                    rvSearchMovies.visible()
                    layoutMain.gone()
                } else {
                    rvSearchMovies.gone()
                    layoutMain.visible()
                    movieTvShowPagingAdapter.submitData(
                        viewLifecycleOwner.lifecycle,
                        PagingData.empty()
                    )
                }
            }
        }
    }

    private fun configureImageSlider() {
        binding?.apply {
            viewPagerImageSlider.apply {
                adapter = movieTvShowSliderPagingAdapter
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
                        if (this@MovieFragment::sliderRunnable.isInitialized) {
                            sliderHandler.removeCallbacks(sliderRunnable)
                            sliderHandler.postDelayed(
                                sliderRunnable, 2000L
                            ) // slide duration 2 seconds
                        }

                        Handler(Looper.getMainLooper()).postDelayed({
                            if (position == 4) {
                                viewPagerImageSlider.setCurrentItem(0, false)
                            }
                        }, 2000L)
                    }
                })
            }
            dotsIndicator.attachTo(viewPagerImageSlider)
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

    private fun setLoadStatePaging(loadState: CombinedLoadStates) {
        when (loadState.refresh) {
            is LoadState.Loading -> binding?.apply {
                shimmerList.visible()
                shimmerList.startShimmer()
                rvSearchMovies.gone()
            }

            is LoadState.NotLoading -> binding?.apply {
                shimmerList.gone()
                shimmerList.stopShimmer()
                rvSearchMovies.visible()
            }

            is LoadState.Error -> binding?.apply {
                shimmerList.gone()
                shimmerList.stopShimmer()
                rvSearchMovies.gone()
            }
        }
    }

    private fun setAllMoviesAdapter() {
        movieTvShowAdapter.submitList(categoryModels)
        binding?.apply {
            rvMovies.adapter = movieTvShowAdapter
            rvMovies.setHasFixedSize(true)
        }
    }

    private fun navigateToDetailPage(id: Int) {
        val navigateToDetailFragment =
            MovieFragmentDirections.actionMovieFragmentToDetailFragment()
        navigateToDetailFragment.id = id
        navigateToDetailFragment.intentFrom = com.application.tmdb.common.Category.MOVIES.name
        findNavController().navigate(navigateToDetailFragment)
    }

    private fun navigateToListPage(category: com.application.tmdb.common.Category, movie: com.application.tmdb.common.Movie) {
        val navigateToListFragment =
            MovieFragmentDirections.actionMovieFragmentToMovieTvShowFragment()
        navigateToListFragment.intentFrom = category.name
        navigateToListFragment.movie = movie
        findNavController().navigate(navigateToListFragment)
    }

    override fun onSeeAllClicked(category: com.application.tmdb.common.Category?, movie: com.application.tmdb.common.Movie?, tvShow: com.application.tmdb.common.TvShow?) {
        navigateToListPage(
            category = category ?: com.application.tmdb.common.Category.MOVIES,
            movie = movie ?: com.application.tmdb.common.Movie.POPULAR_MOVIES
        )
    }

    override fun onItemClicked(data: MovieTvShowModel?) {
        navigateToDetailPage(data?.id ?: 0)
    }

    override fun onSearchStateChanged(enabled: Boolean) {
        movieViewModel.setIsSearchStateChanged(enabled)
    }

    override fun onSearchConfirmed(text: CharSequence?) {
        movieViewModel.getListMovies(
            movie = null,
            page = com.application.tmdb.common.Page.MORE_THAN_ONE,
            query = text.toString(),
            movieId = null,
            rxDisposer = com.application.tmdb.common.RxDisposer().apply { bind(viewLifecycleOwner.lifecycle) }
        )

        // hide keyboard after search
        requireActivity().hideKeyboard()
    }

    override fun onButtonClicked(buttonCode: Int) {}

    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

    override fun afterTextChanged(text: Editable?) {
        if (text.toString().isEmpty()) {
            movieTvShowPagingAdapter.submitData(
                viewLifecycleOwner.lifecycle,
                PagingData.empty()
            )
        }
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
        categoryModels.clear()
        super.onDestroyView()
    }
}