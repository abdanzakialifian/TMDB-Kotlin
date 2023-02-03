package com.application.zaki.movies.presentation.detail.view

import android.os.Handler
import android.os.Looper
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.paging.LoadState
import com.application.zaki.movies.data.source.remote.paging.combine.ReviewsRxPagingSource
import com.application.zaki.movies.databinding.FragmentDetailBinding
import com.application.zaki.movies.domain.model.movies.DetailMovies
import com.application.zaki.movies.domain.model.tvshows.DetailTvShows
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
import javax.inject.Inject
import kotlin.math.roundToInt

@AndroidEntryPoint
class DetailFragment : BaseVBFragment<FragmentDetailBinding>() {

    @Inject
    lateinit var adapter: ReviewsMoviesPagingAdapter
    private val args: DetailFragmentArgs by navArgs()
    private val detailViewModel by viewModels<DetailViewModel>()

    override fun getViewBinding(): FragmentDetailBinding =
        FragmentDetailBinding.inflate(layoutInflater)

    override fun initView() {
        setDetailInformation(args.id.toString(), args.intentFrom)
        setReviews(args.id.toString())
        binding?.tvSeeAll?.setOnClickListener {
            navigateToReviewsPage()
        }
    }

    private fun setDetailInformation(id: String, intentFrom: String) {
        if (intentFrom == INTENT_FROM_MOVIE) {
            detailViewModel.detailMovies(RxDisposer().apply { bind(lifecycle) }, id)
                .observe(viewLifecycleOwner) { result ->
                    when (result) {
                        is UiState.Loading -> {}
                        is UiState.Success -> {
                            showDataMovie(result.data)
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
                            showDataTvShow(result.data)
                        }
                        is UiState.Error -> {}
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

    private fun setReviews(id: String) {
        detailViewModel.reviewsPaging(
            RxDisposer().apply { bind(lifecycle) },
            id,
            ReviewsRxPagingSource.ONE,
            if (args.intentFrom == INTENT_FROM_MOVIE) ReviewsRxPagingSource.MOVIES else ReviewsRxPagingSource.TV_SHOWS
        )
            .observe(viewLifecycleOwner) { result ->
                adapter.submitData(lifecycle, result)
                adapter.addLoadStateListener { loadState ->
                    when (loadState.refresh) {
                        is LoadState.Loading -> {}
                        is LoadState.NotLoading -> {
                            binding?.apply {
                                rvReviews.adapter = adapter
                                rvReviews.setHasFixedSize(true)
                            }
                        }
                        is LoadState.Error -> {}
                    }
                }
            }
    }

    private fun showDataMovie(data: DetailMovies) {
        val rating = data.voteAverage.toString().toDouble()
        val convertRating = (rating * 100.0).roundToInt() / 100.0
        val genres = ArrayList<String>()
        data.genres?.forEach {
            genres.add(it?.name ?: "")
        }
        val listKey = ArrayList<String>()
        if (data.videos?.results?.isNotEmpty() == true) {
            data.videos.results.forEach {
                listKey.add(it?.key ?: "")
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
            val adapter = CastMovieAdapter()
            adapter.submitList(data.credits?.cast)
            rvCast.adapter = adapter
            rvCast.setHasFixedSize(true)
        }
    }

    private fun showDataTvShow(data: DetailTvShows) {
        binding?.apply {
            val rating = data.voteAverage.toString().toDouble()
            val convertRating = (rating * 100.0).roundToInt() / 100.0
            val genres = ArrayList<String>()
            data.genres?.forEach {
                genres.add(it?.name ?: "")
            }
            val listKey = ArrayList<String>()
            if (data.videos?.results?.isNotEmpty() == true) {
                data.videos.results.forEach {
                    listKey.add(it?.key ?: "")
                }
            } else {
                listKey.add("")
            }
            val randomKey = listKey.random()

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
            tvTitle.text = data.name
            tvSubTitle.text =
                StringBuilder().append(genres.joinToString(", "))
                    .append(" \u25CF ").append(
                        data.firstAirDate?.convertDateText(
                            "dd MMM yy",
                            "yyyy-MM-dd"
                        )
                    )
            tvOverview.text = data.overview
            val adapter = CastTvShowsAdapter()
            adapter.submitList(data.credits?.cast)
            rvCast.adapter = adapter
            rvCast.setHasFixedSize(true)
        }
    }

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