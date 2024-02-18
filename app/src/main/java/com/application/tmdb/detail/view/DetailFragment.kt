package com.application.tmdb.detail.view

import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.application.tmdb.common.R
import com.application.tmdb.common.base.BaseVBFragment
import com.application.tmdb.common.model.DetailModel
import com.application.tmdb.common.utils.AppBarStateChangedListener
import com.application.tmdb.common.utils.Category
import com.application.tmdb.common.utils.Constant
import com.application.tmdb.common.utils.RxDisposer
import com.application.tmdb.common.utils.State
import com.application.tmdb.common.utils.UiState
import com.application.tmdb.common.utils.convertDateText
import com.application.tmdb.common.utils.fromMinutesToHHmm
import com.application.tmdb.common.utils.gone
import com.application.tmdb.common.utils.loadBackdropImageUrl
import com.application.tmdb.common.utils.loadImageUrl
import com.application.tmdb.common.utils.visible
import com.application.tmdb.databinding.FragmentDetailBinding
import com.application.tmdb.detail.adapter.DetailPagerAdapter
import com.application.tmdb.detail.viewmodel.DetailViewModel
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipDrawable
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import java.math.RoundingMode

@AndroidEntryPoint
class DetailFragment : BaseVBFragment<FragmentDetailBinding>() {
    private val detailViewModel by viewModels<DetailViewModel>()

    private var userScoreProgress = 0

    private var userScorePercentage = 0

    private var id: Int? = null

    private var intentFrom: String? = null

    override fun getViewBinding(): FragmentDetailBinding =
        FragmentDetailBinding.inflate(layoutInflater)

    override fun initView() {
        id = arguments?.getInt(Constant.KEY_ID)
        intentFrom = arguments?.getString(Constant.KEY_INTENT_FROM)

        if (intentFrom == Category.MOVIES.name) {
            if (detailViewModel.detailData.value == null) {
                detailViewModel.detailMovies(
                    movieId = (id ?: 0).toString(),
                    rxDisposer = RxDisposer()
                        .apply { bind(viewLifecycleOwner.lifecycle) }
                )
            }
        } else {
            if (detailViewModel.detailData.value == null) {
                detailViewModel.detailTvShows(
                    tvId = (id ?: 0).toString(),
                    rxDisposer = RxDisposer().apply { bind(viewLifecycleOwner.lifecycle) }
                )
            }
        }

        handlePhysicalBackButton()
        observeData(intentFrom.orEmpty())
        setViewPager()
    }

    private fun observeData(intentFrom: String) {
        detailViewModel.detailDataState.observe(viewLifecycleOwner) { result ->
            when (result) {
                is UiState.Loading -> {
                    binding?.apply {
                        shimmerDetail.visible()
                        shimmerDetail.startShimmer()
                        layoutDetail.gone()
                    }
                    setAppBarLayout("")
                }

                is UiState.Success -> {
                    binding?.apply {
                        shimmerDetail.gone()
                        shimmerDetail.stopShimmer()
                        layoutDetail.visible()
                    }
                    val detail = result.data
                    setAppBarLayout(detail.title)
                    showDataDetail(detail)
                    detailViewModel.setDetailData(intentFrom, detail)
                }

                is UiState.Error -> {}
                is UiState.Empty -> {}
            }
        }
    }

    private fun showDataDetail(data: DetailModel) {
        val convertRating =
            data.voteAverage?.toBigDecimal()?.setScale(1, RoundingMode.UP)?.toDouble()
        userScorePercentage = convertRating.toString().replace(".", "").toInt()

        data.genres?.forEach {
            addChipToGroup(it.name.orEmpty())
        }

        binding?.apply {
            imgBackdrop.loadBackdropImageUrl(data.backdropPath.orEmpty())
            imgPoster.loadImageUrl(data.posterPath.orEmpty())
            tvTitle.text = data.title
            if (!data.originalLanguage.isNullOrEmpty() && !data.certification.isNullOrEmpty()) {
                tvLanguage.text = data.originalLanguage
                tvCertification.text = data.certification
            } else {
                tvLanguage.gone()
                tvCertification.gone()
            }

            tvSubTitle.text = StringBuilder().append(data.runtime?.fromMinutesToHHmm() ?: "0h 0min")
                .append(" ${Constant.FORMAT_ICON_BULLET} ")
                .append(
                    data.releaseDate?.convertDateText(
                        Constant.FORMAT_DD_MMM_YYYY,
                        Constant.FORMAT_YYYY_MM_DD,
                    )
                )
        }

        userScoreProgress()
    }

    private fun setAppBarLayout(title: String?) {
        binding?.apply {
            appBarLayout.addOnOffsetChangedListener(object : AppBarStateChangedListener() {
                override fun onStateChanged(appBarLayout: AppBarLayout?, state: State?) {
                    if (state == State.COLLAPSED) {
                        imgBack.visible()
                        imgBack.setOnClickListener {
                            navigateToMovieOrTvShow(intentFrom.orEmpty())
                        }
                        collapsingToolbarLayout.title = title
                        toolbar.setBackgroundColor(
                            ContextCompat.getColor(
                                requireContext(),
                                R.color.dark_red
                            )
                        )
                    } else {
                        imgBack.gone()
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

    private fun setViewPager() {
        val fragments = listOf(
            OverviewFragment(),
            CastCrewFragment(),
            ReviewsFragment(),
            SimilarFragment()
        )
        val titles = listOf(
            REGEX.replace(resources.getString(R.string.overview), ""),
            resources.getString(R.string.castandcrew),
            resources.getString(R.string.reviews),
            resources.getString(R.string.similar)
        )
        binding?.apply {
            val detailPagerAdapter =
                DetailPagerAdapter(childFragmentManager, viewLifecycleOwner.lifecycle)
            detailPagerAdapter.setFragments(fragments)
            viewPager.adapter = detailPagerAdapter
            viewPager.isUserInputEnabled = false
            TabLayoutMediator(tabLayout, viewPager) { tab, position ->
                tab.text = titles[position]
            }.attach()
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

    private fun userScoreProgress() {
        val handler = Handler(Looper.getMainLooper())
        handler.postDelayed(object : Runnable {
            override fun run() {
                if (userScoreProgress <= userScorePercentage) {
                    binding?.apply {
                        tvUserScore.text = StringBuilder().append(userScoreProgress).append("%")
                        progressUserScore.progress = userScoreProgress
                        userScoreProgress++
                    }
                    handler.postDelayed(this, 10L)
                } else {
                    handler.removeCallbacks(this)
                }
            }

        }, 10L)
    }

    private fun handlePhysicalBackButton() {
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    navigateToMovieOrTvShow(intentFrom.orEmpty())
                }
            })
    }

    private fun navigateToMovieOrTvShow(intentFrom: String) {
        if (intentFrom == Category.MOVIES.name) {
            findNavController().popBackStack(com.application.tmdb.movie.R.id.movie_fragment, false)
        } else {
            findNavController().popBackStack(com.application.tmdb.tv.R.id.tv_show_fragment, false)
        }
    }

    companion object {
        private val REGEX = Regex("[^A-Za-z0-9 ]")
    }
}