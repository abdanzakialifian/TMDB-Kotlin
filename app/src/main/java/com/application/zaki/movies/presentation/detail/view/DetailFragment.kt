package com.application.zaki.movies.presentation.detail.view

import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.application.zaki.movies.databinding.FragmentDetailBinding
import com.application.zaki.movies.domain.model.Detail
import com.application.zaki.movies.presentation.base.BaseVBFragment
import com.application.zaki.movies.presentation.detail.adapter.CastMoviesAdapter
import com.application.zaki.movies.presentation.detail.adapter.ReviewsAdapter
import com.application.zaki.movies.presentation.detail.viewmodel.DetailViewModel
import com.application.zaki.movies.utils.Category
import com.application.zaki.movies.utils.RxDisposer
import com.application.zaki.movies.utils.UiState
import com.application.zaki.movies.utils.convertDateText
import com.application.zaki.movies.utils.gone
import com.application.zaki.movies.utils.loadBackdropImageUrl
import com.application.zaki.movies.utils.visible
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlin.math.roundToInt

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
        binding?.tvSeeAll?.setOnClickListener {
            navigateToReviewsPage()
        }
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
        binding?.apply {
            viewLifecycleOwner.lifecycle.addObserver(youtubePlayerView)
            youtubePlayerView.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
                override fun onError(
                    youTubePlayer: YouTubePlayer,
                    error: PlayerConstants.PlayerError,
                ) {
                    showBackdropImage()
                    youTubePlayer.seekTo(0F)
                }

                override fun onReady(youTubePlayer: YouTubePlayer) {
                    youTubePlayer.loadVideo(keyYoutube, 0F)
                }

                override fun onStateChange(
                    youTubePlayer: YouTubePlayer,
                    state: PlayerConstants.PlayerState,
                ) {
                    if (state == PlayerConstants.PlayerState.ENDED) {
                        showBackdropImage()
                        youTubePlayer.seekTo(0F)
                    }
                }
            })
        }
    }

    private fun showDataDetail(data: Detail) {
        val rating = data.voteAverage.toString().toDouble()
        val convertRating = (rating * 100.0).roundToInt() / 100.0
        val genres = ArrayList<String>()
        data.genres?.forEach {
            genres.add(it.name ?: "")
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
            imgDetail.loadBackdropImageUrl(data.backdropPath ?: "")
            playAnimation.setOnClickListener {
                playAnimation.playAnimation()
                Handler(Looper.getMainLooper()).postDelayed({
                    setYoutubeTrailer(randomKey)
                    imgDetail.gone()
                    playAnimation.gone()
                    youtubePlayerView.visible()
                }, DELAY_PLAY_TRAILER_YOUTUBE)
            }
            tvRating.text = convertRating.toString()
            tvVote.text = data.voteCount.toString()
            tvTitle.text = data.title
            tvSubTitle.text =
                StringBuilder().append(genres.joinToString(", "))
                    .append(" \u25CF ").append(
                        data.releaseDate?.convertDateText(
                            "dd MMM yy",
                            "yyyy-MM-dd"
                        )
                    )
            tvOverview.text = data.overview

            castMoviesAdapter.submitList(data.cast)
            rvCast.adapter = castMoviesAdapter
            rvCast.setHasFixedSize(true)

            reviewsAdapter.submitList(data.reviews)
            rvReviews.adapter = reviewsAdapter
            rvReviews.setHasFixedSize(true)
        }
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
        binding?.apply {
            imgDetail.visible()
            playAnimation.visible()
            youtubePlayerView.gone()
        }
    }

    override fun onDestroy() {
        binding?.youtubePlayerView?.release()
        super.onDestroy()
    }

    companion object {
        private const val DELAY_PLAY_TRAILER_YOUTUBE = 600L
        const val INTENT_FROM_MOVIE = "Intent From Movie"
        const val INTENT_FROM_TV_SHOWS = "Intent From Tv Shows"
    }
}