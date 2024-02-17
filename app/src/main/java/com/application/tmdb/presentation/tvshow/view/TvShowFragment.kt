package com.application.tmdb.presentation.tvshow.view

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
import com.application.tmdb.databinding.FragmentTvShowBinding
import com.application.tmdb.core.domain.model.CategoryModel
import com.application.tmdb.core.domain.model.MovieTvShowModel
import com.application.tmdb.presentation.adapter.MovieTvShowAdapter
import com.application.tmdb.presentation.adapter.MovieTvShowSliderPagingAdapter
import com.application.tmdb.presentation.base.BaseVBFragment
import com.application.tmdb.presentation.movietvshow.adapter.MovieTvShowPagingAdapter
import com.application.tmdb.presentation.tvshow.viewmodel.TvShowViewModel
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
class TvShowFragment : BaseVBFragment<FragmentTvShowBinding>(),
    MovieTvShowSliderPagingAdapter.OnItemClickCallback, MovieTvShowAdapter.OnEventClickCallback,
    MaterialSearchBar.OnSearchActionListener, TextWatcher,
    MovieTvShowPagingAdapter.OnItemClickCallback {

    private val movieTvShowSliderPagingAdapter by lazy { MovieTvShowSliderPagingAdapter() }

    private val movieTvShowAdapter by lazy { MovieTvShowAdapter() }

    private val movieTvShowPagingAdapter by lazy { MovieTvShowPagingAdapter() }

    private lateinit var sliderRunnable: Runnable

    private val tvShowViewModel by viewModels<TvShowViewModel>()

    private val categoryModels = mutableListOf<CategoryModel>()

    private val sliderHandler = Handler(Looper.getMainLooper())

    override fun getViewBinding(): FragmentTvShowBinding =
        FragmentTvShowBinding.inflate(layoutInflater)

    override fun initView() {
        sliderRunnable = Runnable {
            binding?.viewPagerImageSlider?.apply {
                currentItem += 1
            }
        }

        if (tvShowViewModel.listTvShowsPaging.value == null) {
            tvShowViewModel.getListAllTvShows(
                airingTodayTvShow = com.application.tmdb.common.TvShow.AIRING_TODAY_TV_SHOWS,
                topRatedTvShow = com.application.tmdb.common.TvShow.TOP_RATED_TV_SHOWS,
                popularTvShow = com.application.tmdb.common.TvShow.POPULAR_TV_SHOWS,
                onTheAirTvShow = com.application.tmdb.common.TvShow.ON_THE_AIR_TV_SHOWS,
                page = com.application.tmdb.common.Page.ONE,
                query = null,
                tvId = null,
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
            searchBar.setOnSearchActionListener(this@TvShowFragment)

            searchBar.addTextChangeListener(this@TvShowFragment)
        }
    }

    private fun observeData() {
        tvShowViewModel.listTvShowsPaging.observe(viewLifecycleOwner) { result ->
            result.forEachIndexed { index, pairTvShow ->
                val tvShow = pairTvShow.first
                val tvShowPaging = pairTvShow.second

                if (tvShow == com.application.tmdb.common.TvShow.AIRING_TODAY_TV_SHOWS) {
                    configureImageSlider()
                    movieTvShowSliderPagingAdapter.submitData(
                        viewLifecycleOwner.lifecycle,
                        tvShowPaging
                    )
                    movieTvShowSliderPagingAdapter.addLoadStateListener { loadState ->
                        setLoadStatePagingSlider(loadState)
                    }
                } else {
                    val title = when (tvShow) {
                        com.application.tmdb.common.TvShow.TOP_RATED_TV_SHOWS -> resources.getString(R.string.top_rated_tv_shows)
                        com.application.tmdb.common.TvShow.POPULAR_TV_SHOWS -> resources.getString(R.string.popular_tv_shows)
                        else -> resources.getString(R.string.on_the_air_tv_shows)
                    }

                    val data = CategoryModel(
                        categoryId = index,
                        categoryTitle = title,
                        categories = tvShowPaging,
                        category = com.application.tmdb.common.Category.TV_SHOWS,
                        tvShow = tvShow
                    )
                    categoryModels.add(data)
                }
            }
            setAllTvShowsAdapter()
        }

        tvShowViewModel.listSearchTvShowsPaging.observe(viewLifecycleOwner) { result ->
            movieTvShowPagingAdapter.submitData(viewLifecycleOwner.lifecycle, result)
            movieTvShowPagingAdapter.setOnItemClickCallback(this)
            binding?.apply {
                rvSearchTvShows.adapter = movieTvShowPagingAdapter
                rvSearchTvShows.setHasFixedSize(true)
            }
            movieTvShowPagingAdapter.addLoadStateListener { loadState ->
                setLoadStatePaging(loadState)
            }
        }

        tvShowViewModel.isSearchStateChanged.observe(viewLifecycleOwner) { isSearchStateChanged ->
            binding?.apply {
                if (isSearchStateChanged) {
                    rvSearchTvShows.visible()
                    layoutMain.gone()
                } else {
                    rvSearchTvShows.gone()
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
                        if (this@TvShowFragment::sliderRunnable.isInitialized) {
                            sliderHandler.removeCallbacks(sliderRunnable)
                            sliderHandler.postDelayed(
                                sliderRunnable,
                                2000L
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
                rvSearchTvShows.gone()
            }

            is LoadState.NotLoading -> binding?.apply {
                shimmerList.gone()
                shimmerList.stopShimmer()
                rvSearchTvShows.visible()
            }

            is LoadState.Error -> binding?.apply {
                shimmerList.gone()
                shimmerList.stopShimmer()
                rvSearchTvShows.gone()
            }
        }
    }

    private fun setAllTvShowsAdapter() {
        movieTvShowAdapter.submitList(categoryModels)
        binding?.apply {
            rvTvShows.adapter = movieTvShowAdapter
            rvTvShows.setHasFixedSize(true)
        }
    }

    private fun navigateToDetailPage(id: Int) {
        val navigateToDetailFragment =
            TvShowFragmentDirections.actionTvShowsFragmentToDetailFragment()
        navigateToDetailFragment.id = id
        navigateToDetailFragment.intentFrom = com.application.tmdb.common.Category.TV_SHOWS.name
        findNavController().navigate(navigateToDetailFragment)
    }

    private fun navigateToListPage(category: com.application.tmdb.common.Category, tvShow: com.application.tmdb.common.TvShow) {
        val navigateToListFragment =
            TvShowFragmentDirections.actionTvShowFragmentToMovieTvShowFragment()
        navigateToListFragment.intentFrom = category.name
        navigateToListFragment.tvShow = tvShow
        findNavController().navigate(navigateToListFragment)
    }

    override fun onSeeAllClicked(category: com.application.tmdb.common.Category?, movie: com.application.tmdb.common.Movie?, tvShow: com.application.tmdb.common.TvShow?) {
        navigateToListPage(
            category = category ?: com.application.tmdb.common.Category.TV_SHOWS,
            tvShow = tvShow ?: com.application.tmdb.common.TvShow.POPULAR_TV_SHOWS
        )
    }

    override fun onItemClicked(data: MovieTvShowModel?) {
        navigateToDetailPage(data?.id ?: 0)
    }

    override fun onSearchStateChanged(enabled: Boolean) {
        tvShowViewModel.setIsSearchStateChanged(enabled)
    }

    override fun onSearchConfirmed(text: CharSequence?) {
        tvShowViewModel.getListTvShows(
            tvShow = null,
            page = com.application.tmdb.common.Page.MORE_THAN_ONE,
            query = text.toString(),
            tvId = null,
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