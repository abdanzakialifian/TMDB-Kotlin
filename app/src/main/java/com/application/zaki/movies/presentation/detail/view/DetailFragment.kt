package com.application.zaki.movies.presentation.detail.view

import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.paging.LoadState
import com.application.zaki.movies.databinding.FragmentDetailBinding
import com.application.zaki.movies.presentation.base.BaseVBFragment
import com.application.zaki.movies.presentation.detail.adapter.CastMovieAdapter
import com.application.zaki.movies.presentation.detail.adapter.CastTvShowsAdapter
import com.application.zaki.movies.presentation.detail.adapter.ReviewsMoviesPagingAdapter
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
                                val rating = result.data.voteAverage.toString().toDouble()
                                val convertRating = (rating * 100.0).roundToInt() / 100.0
                                val genres = ArrayList<String>()
                                result.data.genres?.forEach { data ->
                                    genres.add(data?.name ?: "")
                                }
                                val listKey = ArrayList<String>()
                                result.data.videos?.results?.forEach {
                                    listKey.add(it?.key ?: "")
                                }
                                val randomKey = listKey.random()

                                binding?.apply {
                                    imgDetail.loadBackdropImageUrl(result.data.backdropPath ?: "")
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
                                    val rating = result.data.voteAverage.toString().toDouble()
                                    val convertRating = (rating * 100.0).roundToInt() / 100.0
                                    val genres = ArrayList<String>()
                                    result.data.genres?.forEach { data ->
                                        genres.add(data?.name ?: "")
                                    }
                                    val listKey = ArrayList<String>()
                                    result.data.videos?.results?.forEach {
                                        listKey.add(it?.key ?: "")
                                    }
                                    val randomKey = listKey.random()

                                    imgDetail.loadBackdropImageUrl(result.data.backdropPath ?: "")
                                    playAnimation.setOnClickListener {
                                        playAnimation.playAnimation()
                                        Handler(Looper.getMainLooper()).postDelayed({
                                            if (result.data.videos?.results != null && result.data.videos.results.isNotEmpty()) {
                                                setYoutubeTrailer(randomKey)
                                                imgDetail.gone()
                                                playAnimation.gone()
                                                youtubePlayerView.visible()
                                            }
                                        }, DELAY_PLAY_TRAILER_YOUTUBE)
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
        detailViewModel.reviewsMoviesPaging(RxDisposer().apply { bind(lifecycle) }, id)
            .observe(viewLifecycleOwner) { result ->
                val adapter = ReviewsMoviesPagingAdapter()
                adapter.submitData(lifecycle, result)
                binding?.apply {
                    rvReviews.adapter = adapter
                    rvReviews.setHasFixedSize(true)
                }
                adapter.addLoadStateListener { loadState ->
                    when (loadState.refresh) {
                        is LoadState.Loading -> Log.d("LOG", "setReviews: LOADING")
                        is LoadState.NotLoading -> Log.d("LOG", "setReviews: MASUK")
                        is LoadState.Error -> Log.d("LOG", "setReviews: ERROR")
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