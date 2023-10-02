package com.application.zaki.movies.presentation.detail.view

import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.application.zaki.movies.R
import com.application.zaki.movies.databinding.FragmentDetailBinding
import com.application.zaki.movies.domain.model.Detail
import com.application.zaki.movies.presentation.base.BaseVBFragment
import com.application.zaki.movies.presentation.detail.adapter.CastMoviesAdapter
import com.application.zaki.movies.presentation.detail.adapter.DetailPagerAdapter
import com.application.zaki.movies.presentation.detail.adapter.ReviewsAdapter
import com.application.zaki.movies.presentation.detail.viewmodel.DetailViewModel
import com.application.zaki.movies.utils.AppBarStateChangedListener
import com.application.zaki.movies.utils.Category
import com.application.zaki.movies.utils.RxDisposer
import com.application.zaki.movies.utils.UiState
import com.application.zaki.movies.utils.convertDateText
import com.application.zaki.movies.utils.fromMinutesToHHmm
import com.application.zaki.movies.utils.loadBackdropImageUrl
import com.application.zaki.movies.utils.loadImageUrl
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipDrawable
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import java.math.RoundingMode
import javax.inject.Inject


@AndroidEntryPoint
class DetailFragment : BaseVBFragment<FragmentDetailBinding>() {
    @Inject
    lateinit var castMoviesAdapter: CastMoviesAdapter

    @Inject
    lateinit var reviewsAdapter: ReviewsAdapter

    private val args: DetailFragmentArgs by navArgs()

    private val detailViewModel by viewModels<DetailViewModel>()

    override fun getViewBinding(): FragmentDetailBinding =
        FragmentDetailBinding.inflate(layoutInflater)

    override fun initView() {
        val id = args.id.toString()
        val intentFrom = args.intentFrom

        if (intentFrom == Category.MOVIES.name) {
            if (detailViewModel.detailData.value == null) {
                detailViewModel.detailMovies(
                    movieId = id,
                    rxDisposer = RxDisposer().apply { bind(viewLifecycleOwner.lifecycle) })
            }
        } else {
            if (detailViewModel.detailData.value == null) {
                detailViewModel.detailTvShows(
                    tvId = id,
                    rxDisposer = RxDisposer().apply { bind(viewLifecycleOwner.lifecycle) })
            }
        }

        observeData(intentFrom)
    }

    private fun observeData(intentFrom: String) {
        detailViewModel.detailDataState.observe(viewLifecycleOwner) { result ->
            when (result) {
                is UiState.Loading -> {}
                is UiState.Success -> {
                    showDataDetail(result.data)
                    detailViewModel.setDetailData(intentFrom, result.data)
                }

                is UiState.Error -> {}
                is UiState.Empty -> {}
            }
        }
    }

    private fun showDataDetail(data: Detail) {
        val convertRating =
            data.voteAverage?.toBigDecimal()?.setScale(1, RoundingMode.UP)?.toDouble()
        data.genres?.forEach {
            addChipToGroup(it.name ?: "")
        }
        setViewPager(data)

        binding?.apply {
            imgBackdrop.loadBackdropImageUrl(data.backdropPath ?: "")
            imgPoster.loadImageUrl(data.posterPath ?: "")
            tvTitle.text = data.title
            tvLanguage.text = data.originalLanguage
            tvSubTitle.text = StringBuilder()
                .append(data.runtime?.fromMinutesToHHmm() ?: "0h 0min")
                .append(" \u25CF ")
                .append(data.releaseDate?.convertDateText("dd MMM yyyy", "yyyy-MM-dd"))
            tvRating.text = (convertRating ?: 0).toString()
            appBarLayout.addOnOffsetChangedListener(object : AppBarStateChangedListener() {
                override fun onStateChanged(appBarLayout: AppBarLayout?, state: State?) {
                    if (state == State.COLLAPSED) {
                        collapsingToolbarLayout.title = data.title
                        toolbar.setBackgroundColor(
                            ContextCompat.getColor(
                                requireContext(),
                                R.color.dark_red
                            )
                        )
                    } else {
                        collapsingToolbarLayout.title = ""
                        toolbar.setBackgroundColor(
                            ContextCompat.getColor(
                                requireContext(),
                                android.R.color.transparent
                            )
                        )
                    }
                }
            })
        }
    }

    private fun setViewPager(data: Detail) {
        val fragments =
            listOf(OverviewFragment(), CastCrewFragment(), ReviewsFragment(), SimilarFragment())
        val titles = listOf("Overview", "Cast & Crew", "Reviews", "Similar")
        binding?.apply {
            val tabLayout = binding?.tabLayout
            val viewPager = binding?.viewPager
            val detailPagerAdapter =
                DetailPagerAdapter(childFragmentManager, viewLifecycleOwner.lifecycle)
            detailPagerAdapter.setFragments(fragments)
            detailPagerAdapter.setDataForOverviewFragment(data.overview ?: "")
            viewPager?.adapter = detailPagerAdapter
            viewPager?.isUserInputEnabled = false
            if (tabLayout != null && viewPager != null) {
                TabLayoutMediator(tabLayout, viewPager) { tab, position ->
                    tab.text = titles[position]
                }.attach()
            }
        }
    }

    private fun addChipToGroup(genre: String) {
        val chip = Chip(requireContext())
        val chipDrawable = ChipDrawable.createFromAttributes(
            requireContext(), null, 0, R.style.ChipStyle
        )
        chip.setTextAppearance(R.style.ChipText)
        chip.setChipDrawable(chipDrawable)
        chip.text = genre
        chip.isCloseIconVisible = false
        chip.isClickable = false
        binding?.chipGroup?.addView(chip as View)
    }

    companion object {
        private const val DELAY_PLAY_TRAILER_YOUTUBE = 600L
        const val INTENT_FROM_MOVIE = "Intent From Movie"
        const val INTENT_FROM_TV_SHOWS = "Intent From Tv Shows"
    }
}