package com.application.tmdb.common.ui.adapter

import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.application.tmdb.common.databinding.ItemListMovieTvShowPagingBinding
import com.application.tmdb.common.model.MovieTvShowModel
import com.application.tmdb.common.utils.convertDateText
import com.application.tmdb.common.utils.loadImageUrl
import java.math.RoundingMode

class MovieTvShowPagingAdapter :
    PagingDataAdapter<MovieTvShowModel, MovieTvShowPagingAdapter.PopularMoviesPagingViewHolder>(
        DIFF_CALLBACK
    ) {

    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    inner class PopularMoviesPagingViewHolder(private val binding: ItemListMovieTvShowPagingBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: MovieTvShowModel?) {
            binding.apply {
                item?.apply {
                    val convertRating =
                        voteAverage?.toBigDecimal()?.setScale(1, RoundingMode.UP)?.toDouble()
                    val userScorePercentage = convertRating.toString().replace(".", "").toInt()

                    imgPoster.loadImageUrl(posterPath ?: "")
                    tvYear.text = releaseDate?.convertDateText("dd MMM yyyy", "yyyy-MM-dd")
                    tvTitle.text = name
                    tvOverview.text = overview
                    userScoreProgress(binding, userScorePercentage)
                }
            }
            if (this@MovieTvShowPagingAdapter::onItemClickCallback.isInitialized) {
                itemView.setOnClickListener {
                    onItemClickCallback.onItemClicked(item)
                }
            }
        }
    }

    private fun userScoreProgress(
        binding: ItemListMovieTvShowPagingBinding,
        userScorePercentage: Int
    ) {
        var userScoreProgress = 0

        val handler = Handler(Looper.getMainLooper())
        handler.postDelayed(object : Runnable {
            override fun run() {
                if (userScoreProgress <= userScorePercentage) {
                    binding.apply {
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

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PopularMoviesPagingViewHolder =
        ItemListMovieTvShowPagingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            .run {
                PopularMoviesPagingViewHolder(this)
            }

    override fun onBindViewHolder(holder: PopularMoviesPagingViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: MovieTvShowModel?)
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<MovieTvShowModel>() {
            override fun areItemsTheSame(
                oldItem: MovieTvShowModel,
                newItem: MovieTvShowModel
            ): Boolean = oldItem.id == newItem.id

            override fun areContentsTheSame(
                oldItem: MovieTvShowModel,
                newItem: MovieTvShowModel
            ): Boolean = oldItem == newItem
        }
    }
}