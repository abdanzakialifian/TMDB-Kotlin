package com.application.zaki.movies.presentation.detail.view

import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.application.zaki.movies.R
import com.application.zaki.movies.databinding.FragmentDetailBinding
import com.application.zaki.movies.domain.model.Detail
import com.application.zaki.movies.presentation.base.BaseVBFragment
import com.application.zaki.movies.presentation.detail.adapter.CastMoviesAdapter
import com.application.zaki.movies.presentation.detail.adapter.DetailPagerAdapter
import com.application.zaki.movies.presentation.detail.adapter.ReviewsAdapter
import com.application.zaki.movies.presentation.detail.viewmodel.DetailViewModel
import com.application.zaki.movies.utils.Category
import com.application.zaki.movies.utils.RxDisposer
import com.application.zaki.movies.utils.UiState
import com.application.zaki.movies.utils.convertDateText
import com.application.zaki.movies.utils.fromMinutesToHHmm
import com.application.zaki.movies.utils.loadBackdropImageUrl
import com.application.zaki.movies.utils.loadImageUrl
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
        setDetailInformation(args.id.toString(), args.intentFrom)
//        binding?.tvSeeAll?.setOnClickListener {
//            navigateToReviewsPage()
//        }
    }

    private fun setDetailInformation(id: String, intentFrom: String) {
        if (intentFrom == Category.MOVIES.name) {
            detailViewModel.detailMovies(RxDisposer().apply { bind(lifecycle) }, id)
                .observe(viewLifecycleOwner) { result ->
                    when (result) {
                        is UiState.Loading -> {}
                        is UiState.Success -> {
                            showDataDetail(result.data)
                        }

                        is UiState.Error -> {}
                        is UiState.Empty -> {}
                    }
                }
        } else {
            detailViewModel.detailTvShows(RxDisposer().apply { bind(lifecycle) }, id)
                .observe(viewLifecycleOwner) { result ->
                    when (result) {
                        is UiState.Loading -> {}
                        is UiState.Success -> {
                            showDataDetail(result.data)
                        }

                        is UiState.Error -> {

                        }

                        is UiState.Empty -> {}
                    }
                }
        }
    }

    private fun setYoutubeTrailer(keyYoutube: String) {
//        binding?.apply {
//            viewLifecycleOwner.lifecycle.addObserver(youtubePlayerView)
//            youtubePlayerView.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
//                override fun onError(
//                    youTubePlayer: YouTubePlayer,
//                    error: PlayerConstants.PlayerError,
//                ) {
//                    showBackdropImage()
//                    youTubePlayer.seekTo(0F)
//                }
//
//                override fun onReady(youTubePlayer: YouTubePlayer) {
//                    youTubePlayer.loadVideo(keyYoutube, 0F)
//                }
//
//                override fun onStateChange(
//                    youTubePlayer: YouTubePlayer,
//                    state: PlayerConstants.PlayerState,
//                ) {
//                    if (state == PlayerConstants.PlayerState.ENDED) {
//                        showBackdropImage()
//                        youTubePlayer.seekTo(0F)
//                    }
//                }
//            })
//        }
    }

    private fun showDataDetail(data: Detail) {
        val convertRating =
            data.voteAverage?.toBigDecimal()?.setScale(1, RoundingMode.UP)?.toDouble()
        val genres = ArrayList<String>()
        data.genres?.forEach {
            genres.add(it.name ?: "")
            addChipToGroup(it.name ?: "")
        }
        val listKey = ArrayList<String>()
        if (data.videos?.isNotEmpty() == true) {
            data.videos.forEach {
                listKey.add(it.key ?: "")
            }
        } else {
            listKey.add("")
        }
        val randomKey = listKey.random()

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
        }

        setViewPager()

//        binding?.apply {
//            imgDetail.loadBackdropImageUrl(data.backdropPath ?: "")
//            playAnimation.setOnClickListener {
//                playAnimation.playAnimation()
//                Handler(Looper.getMainLooper()).postDelayed({
//                    setYoutubeTrailer(randomKey)
//                    imgDetail.gone()
//                    playAnimation.gone()
//                    youtubePlayerView.visible()
//                }, DELAY_PLAY_TRAILER_YOUTUBE)
//            }
//            tvRating.text = convertRating.toString()
//            tvVote.text = data.voteCount.toString()
//            tvTitle.text = data.title
//            tvSubTitle.text =
//                StringBuilder().append(genres.joinToString(", "))
//                    .append(" \u25CF ").append(
//                        data.releaseDate?.convertDateText(
//                            "dd MMM yy",
//                            "yyyy-MM-dd"
//                        )
//                    )
//            tvOverview.text = data.overview
//
//            castMoviesAdapter.submitList(data.cast)
//            rvCast.adapter = castMoviesAdapter
//            rvCast.setHasFixedSize(true)
//
//            reviewsAdapter.submitList(data.reviews)
//            rvReviews.adapter = reviewsAdapter
//            rvReviews.setHasFixedSize(true)
//        }
    }

    private fun setViewPager() {
        val fragments =
            listOf(OverviewFragment(), CastCrewFragment(), ReviewsFragment(), SimilarFragment())
        val titles = listOf("Overview", "Cast & Crew", "Reviews", "Similar")
        binding?.apply {
            val tabLayout = binding?.tabLayout
            val viewPager = binding?.viewPager
            val detailPagerAdapter =
                DetailPagerAdapter(childFragmentManager, viewLifecycleOwner.lifecycle)
            detailPagerAdapter.setFragments(fragments)
            viewPager?.adapter = detailPagerAdapter
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

//    private fun showDataTvShow(data: DetailTvShows) {
//        binding?.apply {
//            val rating = data.voteAverage.toString().toDouble()
//            val convertRating = (rating * 100.0).roundToInt() / 100.0
//            val genres = ArrayList<String>()
//            data.genres?.forEach {
//                genres.add(it.name ?: "")
//            }
//            val listKey = ArrayList<String>()
//            if (data.videos?.results?.isNotEmpty() == true) {
//                data.videos.results.forEach {
//                    listKey.add(it.key ?: "")
//                }
//            } else {
//                listKey.add("")
//            }
//            val randomKey = listKey.random()
//
//            imgDetail.loadBackdropImageUrl(data.backdropPath ?: "")
//            playAnimation.setOnClickListener {
//                playAnimation.playAnimation()
//                Handler(Looper.getMainLooper()).postDelayed({
//                    setYoutubeTrailer(randomKey)
//                    imgDetail.gone()
//                    playAnimation.gone()
//                    youtubePlayerView.visible()
//                }, DELAY_PLAY_TRAILER_YOUTUBE)
//            }
//            tvRating.text = convertRating.toString()
//            tvVote.text = data.voteCount.toString()
//            tvTitle.text = data.name
//            tvSubTitle.text =
//                StringBuilder().append(genres.joinToString(", "))
//                    .append(" \u25CF ").append(
//                        data.firstAirDate?.convertDateText(
//                            "dd MMM yy",
//                            "yyyy-MM-dd"
//                        )
//                    )
//            tvOverview.text = data.overview
//            val adapter = CastTvShowsAdapter()
//            adapter.submitList(data.credits?.cast)
//            rvCast.adapter = adapter
//            rvCast.setHasFixedSize(true)
//        }
//    }

    private fun navigateToReviewsPage() {
        val navigateToReviewsFragment =
            DetailFragmentDirections.actionDetailFragmentToReviewsFragment()
        navigateToReviewsFragment.id = args.id.toString()
        navigateToReviewsFragment.intentFrom = args.intentFrom
        findNavController().navigate(navigateToReviewsFragment)
    }

    private fun showBackdropImage() {
//        binding?.apply {
//            imgDetail.visible()
//            playAnimation.visible()
//            youtubePlayerView.gone()
//        }
    }

    override fun onDestroy() {
//        binding?.youtubePlayerView?.release()
        super.onDestroy()
    }

    companion object {
        private const val DELAY_PLAY_TRAILER_YOUTUBE = 600L
        const val INTENT_FROM_MOVIE = "Intent From Movie"
        const val INTENT_FROM_TV_SHOWS = "Intent From Tv Shows"
    }
}