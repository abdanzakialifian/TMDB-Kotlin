package com.application.zaki.movies.presentation.detail.view

import androidx.fragment.app.viewModels
import com.application.zaki.movies.databinding.FragmentOverviewBinding
import com.application.zaki.movies.presentation.base.BaseVBFragment
import com.application.zaki.movies.presentation.detail.adapter.TrailerAdapter
import com.application.zaki.movies.presentation.detail.viewmodel.DetailViewModel
import com.application.zaki.movies.utils.setResizableText
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class OverviewFragment : BaseVBFragment<FragmentOverviewBinding>() {

    /*
        To ensure that the viewModel is called after
        the child fragment is attached to the parent/host
    */
    private val detailViewModel by viewModels<DetailViewModel>(ownerProducer = { requireParentFragment() })

    @Inject
    lateinit var trailerAdapter: TrailerAdapter

    override fun getViewBinding(): FragmentOverviewBinding =
        FragmentOverviewBinding.inflate(layoutInflater)

    override fun initView() {
        detailViewModel.detailData.observe(viewLifecycleOwner) { pair ->
            val detail = pair.second
            binding?.apply {
                tvOverview.setResizableText(detail.overview ?: "", 3, true, layoutText)
                trailerAdapter.submitList(detail.videos)
                trailerAdapter.setLifecycleOwner(viewLifecycleOwner)
                rvTrailer.adapter = trailerAdapter
                rvTrailer.setHasFixedSize(true)
            }
        }
    }
}