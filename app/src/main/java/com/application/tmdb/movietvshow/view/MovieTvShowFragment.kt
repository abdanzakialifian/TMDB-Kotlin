package com.application.tmdb.movietvshow.view

import android.os.Build
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import com.application.tmdb.common.R
import com.application.tmdb.common.base.BaseVBFragment
import com.application.tmdb.common.model.MovieTvShowModel
import com.application.tmdb.common.ui.adapter.MovieTvShowPagingAdapter
import com.application.tmdb.common.utils.Category
import com.application.tmdb.common.utils.Constant
import com.application.tmdb.common.utils.Movie
import com.application.tmdb.common.utils.Page
import com.application.tmdb.common.utils.RxDisposer
import com.application.tmdb.common.utils.TvShow
import com.application.tmdb.common.utils.gone
import com.application.tmdb.common.utils.visible
import com.application.tmdb.databinding.FragmentMovieTvShowBinding
import com.application.tmdb.movietvshow.viewmodel.MovieTvShowViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MovieTvShowFragment : BaseVBFragment<FragmentMovieTvShowBinding>(),
    MovieTvShowPagingAdapter.OnItemClickCallback {

    private val movieTvShowPagingAdapter by lazy { MovieTvShowPagingAdapter() }

    private val movieTvShowViewModel by viewModels<MovieTvShowViewModel>()

    private var intentFrom: String? = null

    private var movie: Movie? = null

    private var tvShow: TvShow? = null

    override fun getViewBinding(): FragmentMovieTvShowBinding =
        FragmentMovieTvShowBinding.inflate(layoutInflater)

    override fun initView() {
        intentFrom = arguments?.getString(Constant.KEY_INTENT_FROM)

        movie = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arguments?.getSerializable(Constant.KEY_MOVIE, Movie::class.java)
        } else {
            arguments?.getSerializable(Constant.KEY_MOVIE) as? Movie
        }

        tvShow = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arguments?.getSerializable(Constant.KEY_TV_SHOW, TvShow::class.java)
        } else {
            arguments?.getSerializable(Constant.KEY_TV_SHOW) as? TvShow
        }

        when (intentFrom.orEmpty()) {
            Category.MOVIES.name -> {
                binding?.tvTitleAppbar?.text = when (movie) {
                    Movie.POPULAR_MOVIES -> resources.getString(R.string.popular_movies)
                    Movie.TOP_RATED_MOVIES -> resources.getString(R.string.top_rated_movies)
                    else -> resources.getString(R.string.up_coming_movies)
                }
                getListMovies(movie ?: Movie.POPULAR_MOVIES)
            }

            Category.TV_SHOWS.name -> {
                binding?.tvTitleAppbar?.text = when (tvShow) {
                    TvShow.TOP_RATED_TV_SHOWS -> resources.getString(R.string.top_rated_tv_shows)
                    TvShow.POPULAR_TV_SHOWS -> resources.getString(R.string.popular_tv_shows)
                    else -> resources.getString(R.string.on_the_air_tv_shows)
                }
                getListTvShows(tvShow ?: TvShow.POPULAR_TV_SHOWS)
            }
        }

        binding?.imgBack?.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun getListMovies(movie: Movie) {
        if (movieTvShowViewModel.listMoviesPaging.value == null) {
            movieTvShowViewModel.getListMovies(
                movie = movie,
                page = Page.MORE_THAN_ONE,
                query = null,
                movieId = null,
                rxDisposer = RxDisposer().apply { bind(viewLifecycleOwner.lifecycle) },
            )
        }
        observeData()
    }

    private fun getListTvShows(tvShow: TvShow) {
        if (movieTvShowViewModel.listTvShowsPaging.value == null) {
            movieTvShowViewModel.getListTvShows(
                tvShow = tvShow,
                page = Page.MORE_THAN_ONE,
                query = null,
                tvId = null,
                rxDisposer = RxDisposer().apply { bind(viewLifecycleOwner.lifecycle) },
            )
        }
        observeData()
    }

    private fun observeData() {
        movieTvShowViewModel.listMoviesPaging.observe(viewLifecycleOwner) { result ->
            movieTvShowPagingAdapter.submitData(viewLifecycleOwner.lifecycle, result)
            movieTvShowPagingAdapter.setOnItemClickCallback(this)
            binding?.apply {
                rvMovieTvShow.adapter = movieTvShowPagingAdapter
                rvMovieTvShow.setHasFixedSize(true)
            }
            movieTvShowPagingAdapter.addLoadStateListener { loadState ->
                setLoadStatePaging(loadState)
            }
        }

        movieTvShowViewModel.listTvShowsPaging.observe(viewLifecycleOwner) { result ->
            movieTvShowPagingAdapter.submitData(viewLifecycleOwner.lifecycle, result)
            movieTvShowPagingAdapter.setOnItemClickCallback(this)
            binding?.apply {
                rvMovieTvShow.adapter = movieTvShowPagingAdapter
                rvMovieTvShow.setHasFixedSize(true)
            }
            movieTvShowPagingAdapter.addLoadStateListener { loadState ->
                setLoadStatePaging(loadState)
            }
        }
    }

    private fun setLoadStatePaging(loadState: CombinedLoadStates) {
        when (loadState.refresh) {
            is LoadState.Loading -> binding?.apply {
                shimmerList.visible()
                shimmerList.startShimmer()
                rvMovieTvShow.gone()
            }

            is LoadState.NotLoading -> binding?.apply {
                shimmerList.gone()
                shimmerList.stopShimmer()
                rvMovieTvShow.visible()
            }

            is LoadState.Error -> binding?.apply {
                shimmerList.gone()
                shimmerList.stopShimmer()
                rvMovieTvShow.gone()
            }
        }
    }

    private fun navigateToDetailPage(id: Int) {
        val navigateToDetailFragment =
            MovieTvShowFragmentDirections.actionListFragmentToDetailFragment()
        navigateToDetailFragment.id = id
        navigateToDetailFragment.intentFrom = intentFrom.orEmpty()
        findNavController().navigate(navigateToDetailFragment)
    }

    override fun onItemClicked(data: MovieTvShowModel?) {
        navigateToDetailPage(data?.id ?: 0)
    }
}