package com.application.tmdb.presentation.movietvshow.view

import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import com.application.tmdb.R
import com.application.tmdb.databinding.FragmentMovieTvShowBinding
import com.application.tmdb.core.domain.model.MovieTvShowModel
import com.application.tmdb.presentation.base.BaseVBFragment
import com.application.tmdb.presentation.movietvshow.adapter.MovieTvShowPagingAdapter
import com.application.tmdb.presentation.movietvshow.viewmodel.MovieTvShowViewModel
import com.application.tmdb.common.Category
import com.application.tmdb.common.Movie
import com.application.tmdb.common.Page
import com.application.tmdb.common.RxDisposer
import com.application.tmdb.common.TvShow
import com.application.tmdb.common.gone
import com.application.tmdb.common.visible
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MovieTvShowFragment : BaseVBFragment<FragmentMovieTvShowBinding>(),
    MovieTvShowPagingAdapter.OnItemClickCallback {

    private val movieTvShowPagingAdapter by lazy { MovieTvShowPagingAdapter() }

    private val args: MovieTvShowFragmentArgs by navArgs()

    private val movieTvShowViewModel by viewModels<MovieTvShowViewModel>()

    override fun getViewBinding(): FragmentMovieTvShowBinding =
        FragmentMovieTvShowBinding.inflate(layoutInflater)

    override fun initView() {
        val intentFrom = args.intentFrom
        val movie = args.movie
        val tvShow = args.tvShow

        when (intentFrom) {
            com.application.tmdb.common.Category.MOVIES.name -> {
                binding?.tvTitleAppbar?.text = when (movie) {
                    com.application.tmdb.common.Movie.POPULAR_MOVIES -> resources.getString(R.string.popular_movies)
                    com.application.tmdb.common.Movie.TOP_RATED_MOVIES -> resources.getString(R.string.top_rated_movies)
                    else -> resources.getString(R.string.up_coming_movies)
                }
                getListMovies(movie)
            }

            com.application.tmdb.common.Category.TV_SHOWS.name -> {
                binding?.tvTitleAppbar?.text = when (tvShow) {
                    com.application.tmdb.common.TvShow.TOP_RATED_TV_SHOWS -> resources.getString(R.string.top_rated_tv_shows)
                    com.application.tmdb.common.TvShow.POPULAR_TV_SHOWS -> resources.getString(R.string.popular_tv_shows)
                    else -> resources.getString(R.string.on_the_air_tv_shows)
                }
                getListTvShows(tvShow)
            }
        }

        binding?.imgBack?.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun getListMovies(movie: com.application.tmdb.common.Movie) {
        if (movieTvShowViewModel.listMoviesPaging.value == null) {
            movieTvShowViewModel.getListMovies(
                movie = movie,
                page = com.application.tmdb.common.Page.MORE_THAN_ONE,
                query = null,
                movieId = null,
                rxDisposer = com.application.tmdb.common.RxDisposer().apply { bind(viewLifecycleOwner.lifecycle) },
            )
        }
        observeData()
    }

    private fun getListTvShows(tvShow: com.application.tmdb.common.TvShow) {
        if (movieTvShowViewModel.listTvShowsPaging.value == null) {
            movieTvShowViewModel.getListTvShows(
                tvShow = tvShow,
                page = com.application.tmdb.common.Page.MORE_THAN_ONE,
                query = null,
                tvId = null,
                rxDisposer = com.application.tmdb.common.RxDisposer().apply { bind(viewLifecycleOwner.lifecycle) },
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
        val navigateToDetailFragment = MovieTvShowFragmentDirections.actionListFragmentToDetailFragment()
        navigateToDetailFragment.id = id
        navigateToDetailFragment.intentFrom = args.intentFrom
        findNavController().navigate(navigateToDetailFragment)
    }

    override fun onItemClicked(data: MovieTvShowModel?) {
        navigateToDetailPage(data?.id ?: 0)
    }
}