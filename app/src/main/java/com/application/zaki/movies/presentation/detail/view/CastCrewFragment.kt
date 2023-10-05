package com.application.zaki.movies.presentation.detail.view

import androidx.fragment.app.viewModels
import com.application.zaki.movies.databinding.FragmentCastCrewBinding
import com.application.zaki.movies.presentation.base.BaseVBFragment
import com.application.zaki.movies.presentation.detail.adapter.CastCrewAdapter
import com.application.zaki.movies.presentation.detail.viewmodel.DetailViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class CastCrewFragment : BaseVBFragment<FragmentCastCrewBinding>() {

    /*
        To ensure that the viewModel is called after
        the child fragment is attached to the parent/host
    */
    private val detailViewModel by viewModels<DetailViewModel>(ownerProducer = { requireParentFragment() })

    @Inject
    lateinit var castAdapter: CastCrewAdapter

    @Inject
    lateinit var crewAdapter: CastCrewAdapter

    override fun getViewBinding(): FragmentCastCrewBinding =
        FragmentCastCrewBinding.inflate(layoutInflater)

    override fun initView() {
        observeData()
    }

    private fun observeData() {
        detailViewModel.detailData.observe(viewLifecycleOwner) { pair ->
            val detail = pair.second
            castAdapter.submitList(detail.cast)
            crewAdapter.submitList(detail.crew)
            binding?.apply {
                rvCast.adapter = castAdapter
                rvCast.setHasFixedSize(true)
                rvCrew.adapter = crewAdapter
                rvCrew.setHasFixedSize(true)
            }
        }
    }
}