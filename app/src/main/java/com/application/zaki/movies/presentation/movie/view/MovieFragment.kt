package com.application.zaki.movies.presentation.movie.view

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
import com.application.zaki.movies.R
import com.application.zaki.movies.databinding.FragmentMovieBinding
import com.application.zaki.movies.domain.model.CategoryItem
import com.application.zaki.movies.domain.model.MovieTvShow
import com.application.zaki.movies.presentation.adapter.MovieTvShowAdapter
import com.application.zaki.movies.presentation.adapter.MovieTvShowSliderAdapter
import com.application.zaki.movies.presentation.base.BaseVBFragment
import com.application.zaki.movies.presentation.movie.viewmodel.MovieViewModel
import com.application.zaki.movies.presentation.movietvshow.adapter.MovieTvShowPagingAdapter
import com.application.zaki.movies.utils.Category
import com.application.zaki.movies.utils.Movie
import com.application.zaki.movies.utils.Page
import com.application.zaki.movies.utils.RxDisposer
import com.application.zaki.movies.utils.TvShow
import com.application.zaki.movies.utils.gone
import com.application.zaki.movies.utils.hideKeyboard
import com.application.zaki.movies.utils.visible
import com.mancj.materialsearchbar.MaterialSearchBar
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlin.math.abs

@AndroidEntryPoint
class MovieFragment : BaseVBFragment<FragmentMovieBinding>(),
    MovieTvShowSliderAdapter.OnItemClickCallback, MovieTvShowAdapter.OnEventClickCallback,
    MaterialSearchBar.OnSearchActionListener, MovieTvShowPagingAdapter.OnItemClickCallback,
    TextWatcher {

    @Inject
    lateinit var movieTvShowSliderAdapter: MovieTvShowSliderAdapter

    @Inject
    lateinit var movieTvShowAdapter: MovieTvShowAdapter

    @Inject
    lateinit var movieTvShowPagingAdapter: MovieTvShowPagingAdapter

    private lateinit var sliderRunnable: Runnable

    private val movieViewModel by viewModels<MovieViewModel>()

    private val categoryItems = mutableListOf<CategoryItem>()

    private val sliderHandler = Handler(Looper.getMainLooper())

    override fun getViewBinding(): FragmentMovieBinding =
        FragmentMovieBinding.inflate(layoutInflater)

    override fun initView() {
        sliderRunnable = Runnable {
            binding?.viewPagerImageSlider?.apply {
                currentItem += 1
            }
        }

        if (movieViewModel.listMovies.value == null) {
            movieViewModel.getListAllMovies(
                nowPlayingMovie = Movie.NOW_PLAYING_MOVIES,
                topRatedMovie = Movie.TOP_RATED_MOVIES,
                popularMovie = Movie.POPULAR_MOVIES,
                upComingMovie = Movie.UP_COMING_MOVIES,
                page = Page.ONE,
                query = null,
                movieId = null,
                rxDisposer = RxDisposer().apply { bind(viewLifecycleOwner.lifecycle) }
            )
        }

        observeData()

        eventListeners()
    }

    private fun eventListeners() {
        movieTvShowSliderAdapter.setOnItemClickCallback(this)
        movieTvShowAdapter.setOnEventClickCallback(this)
        binding?.apply {
            searchBar.setOnSearchActionListener(this@MovieFragment)
            searchBar.addTextChangeListener(this@MovieFragment)
        }
    }

    private fun observeData() {
        movieViewModel.listMovies.observe(viewLifecycleOwner) { result ->
            result.forEachIndexed { index, pairMovie ->
                val movie = pairMovie.first
                val moviePaging = pairMovie.second

                if (movie == Movie.NOW_PLAYING_MOVIES) {
                    movieTvShowSliderAdapter.submitData(viewLifecycleOwner.lifecycle, moviePaging)
                    configureImageSlider()
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
                        category = Category.MOVIES,
                        movie = movie
                    )
                    categoryItems.add(data)
                }
            }
            setAllMoviesAdapter()
        }

        movieViewModel.listSearchMovies.observe(viewLifecycleOwner) { result ->
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
                adapter = movieTvShowSliderAdapter
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
        movieTvShowAdapter.submitList(categoryItems)
        binding?.apply {
            rvMovies.adapter = movieTvShowAdapter
            rvMovies.setHasFixedSize(true)
        }
    }

    private fun navigateToDetailPage(id: Int) {
        val navigateToDetailFragment = MovieFragmentDirections.actionMovieFragmentToDetailFragment()
        navigateToDetailFragment.id = id
        navigateToDetailFragment.intentFrom = Category.MOVIES.name
        findNavController().navigate(navigateToDetailFragment)
    }

    private fun navigateToListPage(category: Category, movie: Movie, tvShow: TvShow) {
        val navigateToListFragment =
            MovieFragmentDirections.actionMovieFragmentToMovieTvShowFragment()
        navigateToListFragment.intentFrom = category.name
        navigateToListFragment.movie = movie
        navigateToListFragment.tvShow = tvShow
        findNavController().navigate(navigateToListFragment)
    }

    override fun onSeeAllClicked(category: Category?, movie: Movie?, tvShow: TvShow?) {
        navigateToListPage(
            category ?: Category.MOVIES,
            movie ?: Movie.POPULAR_MOVIES,
            tvShow ?: TvShow.POPULAR_TV_SHOWS
        )
    }

    override fun onItemClicked(data: MovieTvShow?) {
        navigateToDetailPage(data?.id ?: 0)
    }

    override fun onSearchStateChanged(enabled: Boolean) {
        movieViewModel.setIsSearchStateChanged(enabled)
    }

    override fun onSearchConfirmed(text: CharSequence?) {
        movieViewModel.getListMovies(
            movie = null,
            page = Page.MORE_THAN_ONE,
            query = text.toString(),
            movieId = null,
            rxDisposer = RxDisposer().apply { bind(viewLifecycleOwner.lifecycle) }
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
        categoryItems.clear()
        super.onDestroyView()
    }
}