package com.application.zaki.movies.presentation.list.view

import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.application.zaki.movies.databinding.FragmentListDiscoverBinding
import com.application.zaki.movies.domain.model.movies.ResultsItemDiscover
import com.application.zaki.movies.presentation.base.BaseVBFragment
import com.application.zaki.movies.presentation.detail.view.DetailFragment
import com.application.zaki.movies.presentation.list.adapter.genres.DiscoverGenresAdapter
import com.application.zaki.movies.presentation.list.viewmodel.DiscoverViewModel
import com.application.zaki.movies.utils.NetworkResult
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
            if (result != null) {
                when (result) {
                    is NetworkResult.Loading -> {}
                    is NetworkResult.Success -> {
                        binding?.apply {
                            tvTitleAppBar.text = args.genreName
                            val adapter = DiscoverGenresAdapter(object :
                                DiscoverGenresAdapter.OnItemClickCallback {
                                override fun onItemClicked(data: ResultsItemDiscover?) {
                                    val navigateToDetailFragment =
                                        ListDiscoverFragmentDirections.actionListDiscoverFragmentToDetailFragment()
                                    navigateToDetailFragment.id = data?.id ?: 0
                                    navigateToDetailFragment.intentFrom =
                                        DetailFragment.INTENT_FROM_MOVIE
                                    findNavController().navigate(navigateToDetailFragment)
                                }
                            })
                            rvDiscover.adapter = adapter
                            adapter.submitData(lifecycle, result.data)
                            rvDiscover.setHasFixedSize(true)
                        }

                    }
                    is NetworkResult.Error -> {}
                    is NetworkResult.Empty -> {}
                }
            }
        }
    }
}