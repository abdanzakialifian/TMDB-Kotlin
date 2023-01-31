package com.application.zaki.movies.presentation.detail.view

import android.os.Handler
import android.os.Looper
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.application.zaki.movies.databinding.FragmentDetailBinding
import com.application.zaki.movies.presentation.base.BaseVBFragment
import com.application.zaki.movies.presentation.detail.adapter.CastMovieAdapter
import com.application.zaki.movies.presentation.detail.adapter.CastTvShowsAdapter
import com.application.zaki.movies.presentation.detail.adapter.ReviewsAdapter
import com.application.zaki.movies.presentation.detail.viewmodel.DetailViewModel
import com.application.zaki.movies.utils.*
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import dagger.hilt.android.AndroidEntryPoint
import kotlin.math.roundToInt

@AndroidEntryPoint
class DetailFragment : BaseVBFragment<FragmentDetailBinding>() {

    private val args: DetailFragmentArgs by navArgs()
    private val detailViewModel by viewModels<DetailViewModel>()

    override fun getViewBinding(): FragmentDetailBinding =
        FragmentDetailBinding.inflate(layoutInflater)

    override fun initView() {
        setDetailInformation(args.id.toString(), args.intentFrom)
        setReviews(args.id.toString())
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

    private fun setDetailInformation(id: String, intentFrom: String) {
        if (intentFrom == INTENT_FROM_MOVIE) {
            detailViewModel.detailMovies(RxDisposer().apply { bind(lifecycle) }, id)
                .observe(viewLifecycleOwner) { result ->
                    if (result != null) {
                        when (result) {
                            is UiState.Loading -> {}
                            is UiState.Success -> {
                                binding?.apply {
                                    imgDetail.loadBackdropImageUrl(result.data.backdropPath ?: "")
                                    playAnimation.setOnClickListener {
                                        playAnimation.playAnimation()
                                        Handler(Looper.getMainLooper()).postDelayed({
                                            setYoutubeTrailer(
                                                result.data.videos?.results?.get(0)?.key
                                                    ?: ""
                                            )
                                            binding?.imgDetail?.gone()
                                            playAnimation.gone()
                                            binding?.youtubePlayerView?.visible()
                                        }, DELAY_PLAY_TRAILER_YOUTUBE)
                                    }
                                    val rating = result.data.voteAverage.toString().toDouble()
                                    val convertRating = (rating * 100.0).roundToInt() / 100.0
                                    val genres = ArrayList<String>()
                                    result.data.genres?.forEach { data ->
                                        genres.add(data?.name ?: "")
                                    }
                                    tvRating.text = convertRating.toString()
                                    tvVote.text = result.data.voteCount.toString()
                                    tvTitle.text = result.data.title
                                    tvSubTitle.text =
                                        StringBuilder().append(genres.joinToString(", "))
                                            .append(" \u25CF ").append(
                                                result.data.releaseDate?.convertDateText(
                                                    "dd MMM yy",
                                                    "yyyy-MM-dd"
                                                )
                                            )
                                    tvOverview.text = result.data.overview
                                    val adapter = CastMovieAdapter()
                                    adapter.submitList(result.data.credits?.cast)
                                    rvCast.adapter = adapter
                                    rvCast.setHasFixedSize(true)
                                }
                            }
                            is UiState.Error -> {}
                            is UiState.Empty -> {}
                        }
                    }
                }
        } else {
            detailViewModel.detailTvShows(RxDisposer().apply { bind(lifecycle) }, id)
                .observe(viewLifecycleOwner) { result ->
                    if (result != null) {
                        when (result) {
                            is UiState.Loading -> {}
                            is UiState.Success -> {
                                binding?.apply {
                                    imgDetail.loadBackdropImageUrl(result.data.backdropPath ?: "")
                                    playAnimation.setOnClickListener {
                                        playAnimation.playAnimation()
                                        Handler(Looper.getMainLooper()).postDelayed({
                                            if (result.data.videos?.results != null && result.data.videos.results.isNotEmpty()) {
                                                setYoutubeTrailer(
                                                    result.data.videos.results[0]?.key
                                                        ?: ""
                                                )
                                                binding?.imgDetail?.gone()
                                                playAnimation.gone()
                                                binding?.youtubePlayerView?.visible()
                                            }
                                        }, DELAY_PLAY_TRAILER_YOUTUBE)
                                    }
                                    val rating = result.data.voteAverage.toString().toDouble()
                                    val convertRating = (rating * 100.0).roundToInt() / 100.0
                                    val genres = ArrayList<String>()
                                    result.data.genres?.forEach { data ->
                                        genres.add(data?.name ?: "")
                                    }
                                    tvRating.text = convertRating.toString()
                                    tvVote.text = result.data.voteCount.toString()
                                    tvTitle.text = result.data.name
                                    tvSubTitle.text =
                                        StringBuilder().append(genres.joinToString(", "))
                                            .append(" \u25CF ").append(
                                                result.data.firstAirDate?.convertDateText(
                                                    "dd MMM yy",
                                                    "yyyy-MM-dd"
                                                )
                                            )
                                    tvOverview.text = result.data.overview
                                    val adapter = CastTvShowsAdapter()
                                    adapter.submitList(result.data.credits?.cast)
                                    rvCast.adapter = adapter
                                    rvCast.setHasFixedSize(true)
                                }
                            }
                            is UiState.Error -> {}
                            is UiState.Empty -> {}
                        }
                    }
                }
        }
    }

    private fun setReviews(id: String) {
        detailViewModel.reviewsMovie(RxDisposer().apply { bind(lifecycle) }, id)
            .observe(viewLifecycleOwner) { result ->
                if (result != null) {
                    when (result) {
                        is UiState.Loading -> {}
                        is UiState.Success -> {
                            val adapter = ReviewsAdapter()
                            binding?.rvReviews?.layoutManager =
                                LinearLayoutManager(requireContext())
                            binding?.rvReviews?.adapter = adapter
                            binding?.rvReviews?.setHasFixedSize(true)
                            adapter.setListReview(result.data.results ?: arrayListOf())
                        }
                        is UiState.Error -> {}
                        is UiState.Empty -> {}
                    }
                }
            }
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