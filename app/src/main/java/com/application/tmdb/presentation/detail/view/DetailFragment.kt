package com.application.tmdb.presentation.detail.view

import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.application.tmdb.R
import com.application.tmdb.databinding.FragmentDetailBinding
import com.application.tmdb.domain.model.DetailModel
import com.application.tmdb.presentation.base.BaseVBFragment
import com.application.tmdb.presentation.detail.adapter.DetailPagerAdapter
import com.application.tmdb.presentation.detail.viewmodel.DetailViewModel
import com.application.tmdb.utils.AppBarStateChangedListener
import com.application.tmdb.utils.Category
import com.application.tmdb.utils.RxDisposer
import com.application.tmdb.utils.State
import com.application.tmdb.utils.UiState
import com.application.tmdb.utils.convertDateText
import com.application.tmdb.utils.fromMinutesToHHmm
import com.application.tmdb.utils.gone
import com.application.tmdb.utils.loadBackdropImageUrl
import com.application.tmdb.utils.loadImageUrl
import com.application.tmdb.utils.visible
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipDrawable
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import java.math.RoundingMode

@AndroidEntryPoint
class DetailFragment : BaseVBFragment<FragmentDetailBinding>() {
    private val args: DetailFragmentArgs by navArgs()

    private val detailViewModel by viewModels<DetailViewModel>()

    private var userScoreProgress = 0

    private var userScorePercentage = 0

    override fun getViewBinding(): FragmentDetailBinding =
        FragmentDetailBinding.inflate(layoutInflater)

    override fun initView() {
        val id = args.id.toString()
        val intentFrom = args.intentFrom

        if (intentFrom == Category.MOVIES.name) {
            if (detailViewModel.detailData.value == null) {
                detailViewModel.detailMovies(
                    movieId = id,
                    rxDisposer = RxDisposer().apply { bind(viewLifecycleOwner.lifecycle) }
                )
            }
        } else {
            if (detailViewModel.detailData.value == null) {
                detailViewModel.detailTvShows(
                    tvId = id,
                    rxDisposer = RxDisposer().apply { bind(viewLifecycleOwner.lifecycle) }
                )
            }
        }

        handlePhysicalBackButton()
        observeData(intentFrom)
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
            addChipToGroup(it.name ?: "")
        }

        binding?.apply {
            imgBackdrop.loadBackdropImageUrl(data.backdropPath ?: "")
            imgPoster.loadImageUrl(data.posterPath ?: "")
            tvTitle.text = data.title
            if (!data.originalLanguage.isNullOrEmpty() && !data.certification.isNullOrEmpty()) {
                tvLanguage.text = data.originalLanguage
                tvCertification.text = data.certification
            } else {
                tvLanguage.gone()
                tvCertification.gone()
            }

            tvSubTitle.text = StringBuilder().append(data.runtime?.fromMinutesToHHmm() ?: "0h 0min")
                .append(" \u25CF ")
                .append(data.releaseDate?.convertDateText("dd MMM yyyy", "yyyy-MM-dd"))
        }

        userScoreProgress()
    }

    private fun setAppBarLayout(title: String?) {
        binding?.apply {
            appBarLayout.addOnOffsetChangedListener(object : AppBarStateChangedListener() {
                override fun onStateChanged(appBarLayout: AppBarLayout?, state: State?) {
                    if (state == State.COLLAPSED) {
                        val intentFrom = args.intentFrom
                        imgBack.visible()
                        imgBack.setOnClickListener {
                            navigateToMovieOrTvShow(intentFrom)
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
        val intentFrom = args.intentFrom
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    navigateToMovieOrTvShow(intentFrom)
                }
            })
    }

    private fun navigateToMovieOrTvShow(intentFrom: String) {
        if (intentFrom == Category.MOVIES.name) {
            findNavController().popBackStack(R.id.movie_fragment, false)
        } else {
            findNavController().popBackStack(R.id.tv_show_fragment, false)
        }
    }

    companion object {
        private val REGEX = Regex("[^A-Za-z0-9 ]")
    }
}