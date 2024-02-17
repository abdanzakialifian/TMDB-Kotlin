package com.application.tmdb.presentation.movietvshow.view

import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import com.application.tmdb.R
import com.application.tmdb.common.model.MovieTvShowModel
import com.application.tmdb.common.utils.Category
import com.application.tmdb.common.utils.Movie
import com.application.tmdb.common.utils.Page
import com.application.tmdb.common.utils.RxDisposer
import com.application.tmdb.common.utils.TvShow
import com.application.tmdb.common.utils.gone
import com.application.tmdb.common.utils.visible
import com.application.tmdb.databinding.FragmentMovieTvShowBinding
import com.application.tmdb.presentation.base.BaseVBFragment
import com.application.tmdb.presentation.movietvshow.adapter.MovieTvShowPagingAdapter
import com.application.tmdb.presentation.movietvshow.viewmodel.MovieTvShowViewModel
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
            Category.MOVIES.name -> {
                binding?.tvTitleAppbar?.text = when (movie) {
                    Movie.POPULAR_MOVIES -> resources.getString(R.string.popular_movies)
                    Movie.TOP_RATED_MOVIES -> resources.getString(R.string.top_rated_movies)
                    else -> resources.getString(R.string.up_coming_movies)
                }
                getListMovies(movie)
            }

            Category.TV_SHOWS.name -> {
                binding?.tvTitleAppbar?.text = when (tvShow) {
                    TvShow.TOP_RATED_TV_SHOWS -> resources.getString(R.string.top_rated_tv_shows)
                    TvShow.POPULAR_TV_SHOWS -> resources.getString(R.string.popular_tv_shows)
                    else -> resources.getString(R.string.on_the_air_tv_shows)
                }
                getListTvShows(tvShow)
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
        val navigateToDetailFragment = MovieTvShowFragmentDirections.actionListFragmentToDetailFragment()
        navigateToDetailFragment.id = id
        navigateToDetailFragment.intentFrom = args.intentFrom
        findNavController().navigate(navigateToDetailFragment)
    }

    override fun onItemClicked(data: MovieTvShowModel?) {
        navigateToDetailPage(data?.id ?: 0)
    }
}