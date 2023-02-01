package com.application.zaki.movies.presentation.list.view

import android.util.Log
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.paging.LoadState
import com.application.zaki.movies.databinding.FragmentListDiscoverBinding
import com.application.zaki.movies.domain.model.movies.ResultsItemDiscover
import com.application.zaki.movies.presentation.base.BaseVBFragment
import com.application.zaki.movies.presentation.detail.view.DetailFragment
import com.application.zaki.movies.presentation.list.adapter.genres.DiscoverGenresAdapter
import com.application.zaki.movies.presentation.list.viewmodel.DiscoverViewModel
import com.application.zaki.movies.utils.RxDisposer
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ListDiscoverFragment : BaseVBFragment<FragmentListDiscoverBinding>() {
    private val args: ListDiscoverFragmentArgs by navArgs()
    private val discoverViewModel by viewModels<DiscoverViewModel>()

    override fun getViewBinding(): FragmentListDiscoverBinding =
        FragmentListDiscoverBinding.inflate(layoutInflater)

    override fun initView() {
        discoverViewModel.getDiscoverMovie(
            RxDisposer().apply { bind(lifecycle) },
            args.genreId.toString()
        ).observe(viewLifecycleOwner) { result ->
            binding?.apply {
                tvTitleAppBar.text = args.genreName
                val adapter = DiscoverGenresAdapter(object :
                    DiscoverGenresAdapter.OnItemClickCallback {
                    override fun onItemClicked(data: ResultsItemDiscover?) {
                        navigateToDetailFragment(data?.id ?: 0)
                    }
                })
                adapter.submitData(lifecycle, result)
                rvDiscover.setHasFixedSize(true)
                rvDiscover.adapter = adapter
                adapter.addLoadStateListener { loadState ->
                    when (loadState.refresh) {
                        is LoadState.Loading -> Log.d("LOG", "setReviews: LOADING")
                        is LoadState.NotLoading -> Log.d("LOG", "setReviews: MASUK")
                        is LoadState.Error -> Log.d("LOG", "setReviews: ERROR")
                    }
                }
            }
        }
    }

    private fun navigateToDetailFragment(id: Int) {
        val navigateToDetailFragment =
            ListDiscoverFragmentDirections.actionListDiscoverFragmentToDetailFragment()
        navigateToDetailFragment.id = id
        navigateToDetailFragment.intentFrom =
            DetailFragment.INTENT_FROM_MOVIE
        findNavController().navigate(navigateToDetailFragment)
    }
}