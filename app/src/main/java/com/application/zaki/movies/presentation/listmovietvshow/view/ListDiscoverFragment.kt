package com.application.zaki.movies.presentation.listmovietvshow.view

import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.paging.LoadState
import com.application.zaki.movies.databinding.FragmentListDiscoverBinding
import com.application.zaki.movies.domain.model.DiscoverItem
import com.application.zaki.movies.presentation.base.BaseVBFragment
import com.application.zaki.movies.presentation.detail.view.DetailFragment
import com.application.zaki.movies.presentation.listmovietvshow.adapter.genres.DiscoverGenresAdapter
import com.application.zaki.movies.presentation.listmovietvshow.viewmodel.DiscoverViewModel
import com.application.zaki.movies.utils.Category
import com.application.zaki.movies.utils.RxDisposer
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ListDiscoverFragment : BaseVBFragment<FragmentListDiscoverBinding>() {

    @Inject
    lateinit var adapter: DiscoverGenresAdapter
    private val args: ListDiscoverFragmentArgs by navArgs()
    private val discoverViewModel by viewModels<DiscoverViewModel>()

    override fun getViewBinding(): FragmentListDiscoverBinding =
        FragmentListDiscoverBinding.inflate(layoutInflater)

    override fun initView() {
        discoverViewModel.getDiscover(
            RxDisposer().apply { bind(lifecycle) },
            args.genreId.toString(),
            if (args.intentFrom == DetailFragment.INTENT_FROM_MOVIE) Category.MOVIES else Category.TV_SHOWS
        ).observe(viewLifecycleOwner) { result ->
            binding?.apply {
                tvTitleAppBar.text = args.genreName
                adapter.setOnItemClickCallback(object : DiscoverGenresAdapter.OnItemClickCallback {
                    override fun onItemClicked(data: DiscoverItem?) {
                        navigateToDetailFragment(data?.id ?: 0)
                    }
                })
                adapter.submitData(lifecycle, result)
                rvDiscover.setHasFixedSize(true)
                rvDiscover.adapter = adapter
                adapter.addLoadStateListener { loadState ->
                    when (loadState.refresh) {
                        is LoadState.Loading -> {}
                        is LoadState.NotLoading -> {}
                        is LoadState.Error -> {}
                    }
                }
            }
        }
    }

    private fun navigateToDetailFragment(id: Int) {
        val navigateToDetailFragment =
            ListDiscoverFragmentDirections.actionListDiscoverFragmentToDetailFragment()
        navigateToDetailFragment.id = id
        navigateToDetailFragment.intentFrom = DetailFragment.INTENT_FROM_MOVIE
        findNavController().navigate(navigateToDetailFragment)
    }

    companion object {
        const val INTENT_FROM_MOVIE = "Intent From Movie"
        const val INTENT_FROM_TV_SHOWS = "Intent From Tv Shows"
    }
}