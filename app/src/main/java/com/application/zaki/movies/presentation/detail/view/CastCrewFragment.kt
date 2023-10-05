package com.application.zaki.movies.presentation.detail.view

import androidx.fragment.app.viewModels
import com.application.zaki.movies.R
import com.application.zaki.movies.databinding.FragmentCastCrewBinding
import com.application.zaki.movies.domain.model.CastCrewModel
import com.application.zaki.movies.presentation.base.BaseVBFragment
import com.application.zaki.movies.presentation.detail.adapter.CastCrewAdapter
import com.application.zaki.movies.presentation.detail.viewmodel.DetailViewModel
import com.application.zaki.movies.utils.UiState
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
    lateinit var castCrewAdapter: CastCrewAdapter

    private val castCrewModels = mutableListOf<CastCrewModel>()

    override fun getViewBinding(): FragmentCastCrewBinding =
        FragmentCastCrewBinding.inflate(layoutInflater)

    override fun initView() {
        observeData()
    }

    private fun observeData() {
        detailViewModel.detailDataState.observe(viewLifecycleOwner) { result ->
            when (result) {
                is UiState.Loading -> {
                    binding?.apply {
//                        shimmerOverview.visible()
//                        layoutOverview.gone()
                    }
                }

                is UiState.Success -> {
                    val detail = result.data
                    castCrewModels.addAll(
                        listOf(
                            CastCrewModel(
                                id = 1,
                                title = resources.getString(R.string.cast),
                                castCrews = detail.cast
                            ),
                            CastCrewModel(
                                id = 2,
                                title = resources.getString(R.string.crew),
                                castCrews = detail.crew
                            )
                        )
                    )
                    castCrewAdapter.submitList(castCrewModels)
                    binding?.apply {
                        rvCastCrew.adapter = castCrewAdapter
                        rvCastCrew.setHasFixedSize(true)
                    }
                }

                is UiState.Error -> {}
                is UiState.Empty -> {}
            }
        }
//        detailViewModel.detailData.observe(viewLifecycleOwner) { pair ->
//            val detail = pair.second
//            castAdapter.submitList(detail.cast)
//            crewAdapter.submitList(detail.crew)
//            binding?.apply {
//                rvCast.adapter = castAdapter
//                rvCast.setHasFixedSize(true)
//                rvCrew.adapter = crewAdapter
//                rvCrew.setHasFixedSize(true)
//            }
//        }
    }
}