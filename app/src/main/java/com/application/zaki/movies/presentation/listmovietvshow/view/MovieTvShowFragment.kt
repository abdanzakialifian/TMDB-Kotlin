package com.application.zaki.movies.presentation.listmovietvshow.view

import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.paging.LoadState
import androidx.paging.PagingData
import com.application.zaki.movies.databinding.FragmentListBinding
import com.application.zaki.movies.domain.model.GenresItem
import com.application.zaki.movies.domain.model.MovieTvShow
import com.application.zaki.movies.presentation.base.BaseVBFragment
import com.application.zaki.movies.presentation.listmovietvshow.adapter.MovieTvShowPagingAdapter
import com.application.zaki.movies.presentation.listmovietvshow.viewmodel.MovieTvShowViewModel
import com.application.zaki.movies.utils.Category
import com.application.zaki.movies.utils.Movie
import com.application.zaki.movies.utils.Page
import com.application.zaki.movies.utils.RxDisposer
import com.application.zaki.movies.utils.TvShow
import com.application.zaki.movies.utils.gone
import com.application.zaki.movies.utils.visible
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MovieTvShowFragment : BaseVBFragment<FragmentListBinding>(),
    MovieTvShowPagingAdapter.OnItemClickCallback {

    @Inject
    lateinit var movieTvShowPagingAdapter: MovieTvShowPagingAdapter

    private val args: MovieTvShowFragmentArgs by navArgs()

    private val movieTvShowViewModel by viewModels<MovieTvShowViewModel>()

    override fun getViewBinding(): FragmentListBinding = FragmentListBinding.inflate(layoutInflater)

    override fun initView() {
        val intentFrom = args.intentFrom
        val movie = args.movie
        val tvShow = args.tvShow

        when (intentFrom) {
            Category.MOVIES.name -> setListMovies(movie)
            Category.TV_SHOWS.name -> setListTvShows(tvShow)
        }
    }

    private fun setListMovies(movie: Movie) {
        if (movieTvShowViewModel.listMovies.value == null) {
            movieTvShowViewModel.getListMovies(
                movie = movie,
                category = Category.MOVIES,
                page = Page.MORE_THAN_ONE,
                rxDisposer = RxDisposer().apply { bind(lifecycle) },
            )
        }
        movieTvShowViewModel.listMovies.observe(viewLifecycleOwner) { result ->
            setDataMovieTvShow(result)
        }
    }

    private fun setListTvShows(tvShow: TvShow) {
        if (movieTvShowViewModel.listTvShows.value == null) {
            movieTvShowViewModel.getListTvShows(
                tvShow = tvShow,
                category = Category.MOVIES,
                page = Page.MORE_THAN_ONE,
                rxDisposer = RxDisposer().apply { bind(lifecycle) },
            )
        }
        movieTvShowViewModel.listTvShows.observe(viewLifecycleOwner) { result ->
            setDataMovieTvShow(result)
        }
    }

    private fun setDataMovieTvShow(movieTvShowPaging: PagingData<MovieTvShow>) {
        movieTvShowPagingAdapter.submitData(lifecycle, movieTvShowPaging)
        movieTvShowPagingAdapter.setOnItemClickCallback(this)
        binding?.apply {
            rvListMovies.adapter = movieTvShowPagingAdapter
            rvListMovies.setHasFixedSize(true)
        }
        movieTvShowPagingAdapter.addLoadStateListener { loadState ->
            when (loadState.refresh) {
                is LoadState.Loading -> binding?.apply {
                    shimmerList.visible()
                    shimmerList.startShimmer()
                    rvListMovies.gone()
                }

                is LoadState.NotLoading -> binding?.apply {
                    shimmerList.gone()
                    shimmerList.stopShimmer()
                    rvListMovies.visible()
                }

                is LoadState.Error -> binding?.apply {
                    shimmerList.gone()
                    shimmerList.stopShimmer()
                    rvListMovies.gone()
                }
            }
        }
    }

    private fun navigateToDetailPage(id: Int) {
        val navigateToDetailFragment =
            MovieTvShowFragmentDirections.actionListFragmentToDetailFragment()
        navigateToDetailFragment.id = id
        navigateToDetailFragment.intentFrom = args.intentFrom
        findNavController().navigate(navigateToDetailFragment)
    }

    private fun navigateToDiscoverPage(data: GenresItem) {
        val navigateToDetailFragment =
            MovieTvShowFragmentDirections.actionListFragmentToListDiscoverFragment()
        navigateToDetailFragment.genreId = data.id ?: 0
        navigateToDetailFragment.intentFrom = args.intentFrom
        navigateToDetailFragment.genreName = data.name ?: ""
        findNavController().navigate(navigateToDetailFragment)
    }

    override fun onItemClicked(data: MovieTvShow?) {
        navigateToDetailPage(data?.id ?: 0)
    }

    override fun onItemGenreClicked(data: GenresItem) {
        navigateToDiscoverPage(data)
    }
}