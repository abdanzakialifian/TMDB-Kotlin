package com.application.zaki.movies.presentation.list.view

import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.paging.LoadState
import com.application.zaki.movies.data.source.remote.paging.tvshows.OnTheAirTvShowsRxPagingSource
import com.application.zaki.movies.data.source.remote.paging.tvshows.PopularTvShowsRxPagingSource
import com.application.zaki.movies.data.source.remote.paging.tvshows.TopRatedTvShowsRxPagingSource
import com.application.zaki.movies.databinding.FragmentListBinding
import com.application.zaki.movies.domain.model.genre.GenresItem
import com.application.zaki.movies.domain.model.movies.ListMovies
import com.application.zaki.movies.domain.model.tvshows.ListOnTheAirTvShows
import com.application.zaki.movies.domain.model.tvshows.ListPopularTvShows
import com.application.zaki.movies.domain.model.tvshows.ListTopRatedTvShows
import com.application.zaki.movies.presentation.base.BaseVBFragment
import com.application.zaki.movies.presentation.detail.view.DetailFragment
import com.application.zaki.movies.presentation.list.adapter.movies.PopularMoviesPagingAdapter
import com.application.zaki.movies.presentation.list.adapter.movies.TopRatedMoviesPagingAdapter
import com.application.zaki.movies.presentation.list.adapter.movies.UpComingMoviesPagingAdapter
import com.application.zaki.movies.presentation.list.adapter.tvshows.OnTheAirTvShowsPagingAdapter
import com.application.zaki.movies.presentation.list.adapter.tvshows.PopularTvShowsPagingAdapter
import com.application.zaki.movies.presentation.list.adapter.tvshows.TopRatedTvShowsPagingAdapter
import com.application.zaki.movies.presentation.movies.viewmodel.MoviesViewModel
import com.application.zaki.movies.presentation.tvshows.viewmodel.TvShowsViewModel
import com.application.zaki.movies.utils.*
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import com.application.zaki.movies.utils.Genre.MOVIES
import com.application.zaki.movies.utils.Genre.TV_SHOWS

@AndroidEntryPoint
class ListFragment : BaseVBFragment<FragmentListBinding>() {

    @Inject
    lateinit var popularMoviesAdapter: PopularMoviesPagingAdapter

    @Inject
    lateinit var topRatedMoviesAdapter: TopRatedMoviesPagingAdapter

    @Inject
    lateinit var upComingMoviesAdapter: UpComingMoviesPagingAdapter

    @Inject
    lateinit var onTheAirTvShowsAdapter: OnTheAirTvShowsPagingAdapter

    @Inject
    lateinit var popularTvShowsAdapter: PopularTvShowsPagingAdapter

    @Inject
    lateinit var topRatedTvShowsAdapter: TopRatedTvShowsPagingAdapter

    private val args: ListFragmentArgs by navArgs()
    private val moviesViewModel by viewModels<MoviesViewModel>()
    private val tvShowsViewModel by viewModels<TvShowsViewModel>()

    override fun getViewBinding(): FragmentListBinding =
        FragmentListBinding.inflate(layoutInflater)

    override fun initView() {
        when (args.intentFrom) {
            // set list from movies
            Movie.TOP_RATED_MOVIES -> setListTopRatedMoviesPaging()
            Movie.POPULAR_MOVIES -> setListPopularMoviesPaging()
            else -> setListUpComingMoviesPaging()
            // set list from tv shows
//            INTENT_FROM_TOP_RATED_TV_SHOWS -> setListTopRatedTvShowsPaging()
//            INTENT_FROM_POPULAR_TV_SHOWS -> setListPopularTvShowsPaging()
//            INTENT_FROM_ON_THE_AIR_TV_SHOWS -> setListOnTheAirTvShowsPaging()
        }
    }

    private fun setListTopRatedMoviesPaging() {
        moviesViewModel.getMovies(
            movie = Movie.TOP_RATED_MOVIES,
            genre = MOVIES,
            page = Page.MORE_THAN_ONE,
            rxDisposer =  RxDisposer().apply { bind(lifecycle) },
        )
            .observe(viewLifecycleOwner) { result ->
                topRatedMoviesAdapter.setOnItemClickCallback(object :
                    TopRatedMoviesPagingAdapter.OnItemClickCallback {
                    override fun onItemClicked(data: ListMovies?) {
                        navigateToDetailPage(data?.id ?: 0)
                    }

                    override fun onItemGenreClicked(data: GenresItem) {
                        navigateToDiscoverPage(data, ListDiscoverFragment.INTENT_FROM_MOVIE)
                    }
                })
                topRatedMoviesAdapter.submitData(lifecycle, result)
                binding?.apply {
                    rvListMovies.adapter = topRatedMoviesAdapter
                    rvListMovies.setHasFixedSize(true)
                }
                topRatedMoviesAdapter.addLoadStateListener { loadState ->
                    when (loadState.refresh) {
                        is LoadState.Loading -> showLoading()
                        is LoadState.NotLoading -> visibleDataList()
                        is LoadState.Error -> goneDataList()
                    }
                }
            }
    }

    private fun setListPopularMoviesPaging() {
        moviesViewModel.getMovies(
            movie = Movie.POPULAR_MOVIES,
            genre = MOVIES,
            page = Page.MORE_THAN_ONE,
            rxDisposer =  RxDisposer().apply { bind(lifecycle) },
        )
            .observe(viewLifecycleOwner) { result ->

                popularMoviesAdapter.setOnItemClickCallback(object :
                    PopularMoviesPagingAdapter.OnItemClickCallback {
                    override fun onItemClicked(data: ListMovies?) {
                        navigateToDetailPage(data?.id ?: 0)
                    }

                    override fun onItemGenreClicked(data: GenresItem) {
                        navigateToDiscoverPage(data, ListDiscoverFragment.INTENT_FROM_MOVIE)
                    }
                })
                popularMoviesAdapter.submitData(lifecycle, result)
                binding?.apply {
                    rvListMovies.adapter = popularMoviesAdapter
                    rvListMovies.setHasFixedSize(true)
                }
                popularMoviesAdapter.addLoadStateListener { loadState ->
                    when (loadState.refresh) {
                        is LoadState.Loading -> showLoading()
                        is LoadState.NotLoading -> visibleDataList()
                        is LoadState.Error -> goneDataList()
                    }
                }
            }
    }

    private fun setListUpComingMoviesPaging() {
        moviesViewModel.getMovies(
            movie = Movie.UP_COMING_MOVIES,
            genre = MOVIES,
            page = Page.MORE_THAN_ONE,
            rxDisposer =  RxDisposer().apply { bind(lifecycle) },
        )
            .observe(viewLifecycleOwner) { result ->
                upComingMoviesAdapter.setOnItemClickCallback(object :
                    UpComingMoviesPagingAdapter.OnItemClickCallback {
                    override fun onItemClicked(data: ListMovies?) {
                        navigateToDetailPage(data?.id ?: 0)
                    }

                    override fun onItemGenreClicked(data: GenresItem) {
                        navigateToDiscoverPage(data, ListDiscoverFragment.INTENT_FROM_MOVIE)
                    }
                })
                upComingMoviesAdapter.submitData(lifecycle, result)
                binding?.apply {
                    rvListMovies.adapter = upComingMoviesAdapter
                    rvListMovies.setHasFixedSize(true)
                }
                upComingMoviesAdapter.addLoadStateListener { loadState ->
                    when (loadState.refresh) {
                        is LoadState.Loading -> showLoading()
                        is LoadState.NotLoading -> visibleDataList()
                        is LoadState.Error -> goneDataList()
                    }
                }
            }
    }

//    private fun setListTopRatedTvShowsPaging() {
//        tvShowsViewModel.topRatedTvShowsPaging(
//            RxDisposer().apply { bind(lifecycle) },
//            if (args.intentFrom == DetailFragment.INTENT_FROM_MOVIE) MOVIES else TV_SHOWS,
//            TopRatedTvShowsRxPagingSource.MORE_THAN_ONE
//        )
//            .observe(viewLifecycleOwner) { result ->
//                topRatedTvShowsAdapter.setOnItemClickCallback(object :
//                    TopRatedTvShowsPagingAdapter.OnItemClickCallback {
//                    override fun onItemClicked(data: ListTopRatedTvShows?) {
//                        navigateToDetailPage(data?.id ?: 0)
//                    }
//
//                    override fun onItemGenreClicked(data: GenresItem) {
//                        navigateToDiscoverPage(data, ListDiscoverFragment.INTENT_FROM_TV_SHOWS)
//                    }
//                })
//                topRatedTvShowsAdapter.submitData(lifecycle, result)
//                binding?.apply {
//                    rvListMovies.adapter = topRatedTvShowsAdapter
//                    rvListMovies.setHasFixedSize(true)
//                }
//                topRatedTvShowsAdapter.addLoadStateListener { loadState ->
//                    when (loadState.refresh) {
//                        is LoadState.Loading -> showLoading()
//                        is LoadState.NotLoading -> visibleDataList()
//                        is LoadState.Error -> goneDataList()
//                    }
//                }
//            }
//    }
//
//    private fun setListPopularTvShowsPaging() {
//        tvShowsViewModel.popularTvShowsPaging(
//            RxDisposer().apply { bind(lifecycle) },
//            if (args.intentFrom == DetailFragment.INTENT_FROM_MOVIE) MOVIES else TV_SHOWS,
//            PopularTvShowsRxPagingSource.MORE_THAN_ONE
//        )
//            .observe(viewLifecycleOwner) { result ->
//                popularTvShowsAdapter.setOnItemClickCallback(object :
//                    PopularTvShowsPagingAdapter.OnItemClickCallback {
//                    override fun onItemClicked(data: ListPopularTvShows?) {
//                        navigateToDetailPage(data?.id ?: 0)
//                    }
//
//                    override fun onItemGenreClicked(data: GenresItem) {
//                        navigateToDiscoverPage(data, ListDiscoverFragment.INTENT_FROM_TV_SHOWS)
//                    }
//                })
//                popularTvShowsAdapter.submitData(lifecycle, result)
//                binding?.apply {
//                    rvListMovies.adapter = popularTvShowsAdapter
//                    rvListMovies.setHasFixedSize(true)
//                }
//                popularTvShowsAdapter.addLoadStateListener { loadState ->
//                    when (loadState.refresh) {
//                        is LoadState.Loading -> showLoading()
//                        is LoadState.NotLoading -> visibleDataList()
//                        is LoadState.Error -> goneDataList()
//                    }
//                }
//            }
//    }
//
//    private fun setListOnTheAirTvShowsPaging() {
//        tvShowsViewModel.onTheAirTvShowsPaging(
//            RxDisposer().apply { bind(lifecycle) },
//            if (args.intentFrom == DetailFragment.INTENT_FROM_MOVIE) MOVIES else TV_SHOWS,
//            OnTheAirTvShowsRxPagingSource.MORE_THAN_ONE
//        )
//            .observe(viewLifecycleOwner) { result ->
//                onTheAirTvShowsAdapter.setOnItemClickCallback(object :
//                    OnTheAirTvShowsPagingAdapter.OnItemClickCallback {
//                    override fun onItemClicked(data: ListOnTheAirTvShows?) {
//                        navigateToDetailPage(data?.id ?: 0)
//                    }
//
//                    override fun onItemGenreClicked(data: GenresItem) {
//                        navigateToDiscoverPage(data, ListDiscoverFragment.INTENT_FROM_TV_SHOWS)
//                    }
//                })
//
//                onTheAirTvShowsAdapter.submitData(lifecycle, result)
//                binding?.apply {
//                    rvListMovies.adapter = onTheAirTvShowsAdapter
//                    rvListMovies.setHasFixedSize(true)
//                }
//                onTheAirTvShowsAdapter.addLoadStateListener { loadState ->
//                    when (loadState.refresh) {
//                        is LoadState.Loading -> showLoading()
//                        is LoadState.NotLoading -> visibleDataList()
//                        is LoadState.Error -> goneDataList()
//                    }
//                }
//            }
//    }

    private fun goneDataList() {
        binding?.apply {
            shimmerList.gone()
            shimmerList.stopShimmer()
            rvListMovies.gone()
        }
    }

    private fun visibleDataList() {
        binding?.apply {
            shimmerList.gone()
            shimmerList.stopShimmer()
            rvListMovies.visible()
        }
    }

    private fun showLoading() {
        binding?.apply {
            shimmerList.visible()
            shimmerList.startShimmer()
            rvListMovies.gone()
        }
    }

    private fun navigateToDetailPage(id: Int) {
        val navigateToDetailFragment =
            ListFragmentDirections.actionListFragmentToDetailFragment()
        navigateToDetailFragment.id = id
        navigateToDetailFragment.intentFrom = DetailFragment.INTENT_FROM_MOVIE
        findNavController().navigate(navigateToDetailFragment)
    }

    private fun navigateToDiscoverPage(data: GenresItem, intentFrom: String) {
        val navigateToDetailFragment =
            ListFragmentDirections.actionListFragmentToListDiscoverFragment()
        navigateToDetailFragment.genreId = data.id ?: 0
        navigateToDetailFragment.intentFrom = intentFrom
        navigateToDetailFragment.genreName = data.name ?: ""
        findNavController().navigate(navigateToDetailFragment)
    }

    companion object {
        // intent from movies
        const val INTENT_FROM_POPULAR_MOVIES = "Popular Movies"
        const val INTENT_FROM_TOP_RATED_MOVIES = "Top rated movies"
        const val INTENT_FROM_UP_COMING_MOVIES = "Up coming movies"

        // intent from tv shows
        const val INTENT_FROM_ON_THE_AIR_TV_SHOWS = "Intent On The Air TV Shows"
        const val INTENT_FROM_POPULAR_TV_SHOWS = "Intent Popular TV Shows"
        const val INTENT_FROM_TOP_RATED_TV_SHOWS = "Intent Top Rated TV Shows"
    }
}