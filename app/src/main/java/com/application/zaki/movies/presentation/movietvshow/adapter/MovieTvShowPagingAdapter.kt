package com.application.zaki.movies.presentation.movietvshow.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.application.zaki.movies.databinding.ItemListMovieTvShowPagingBinding
import com.application.zaki.movies.domain.model.MovieTvShowModel
import com.application.zaki.movies.utils.convertDateText
import com.application.zaki.movies.utils.loadImageUrl
import java.math.RoundingMode
import javax.inject.Inject

class MovieTvShowPagingAdapter @Inject constructor() :
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
                    posterPath?.let { url ->
                        imgPoster.loadImageUrl(url)
                    }

                    releaseDate?.let { releaseDate ->
                        tvYear.text = releaseDate.convertDateText("dd MMM yyyy", "yyyy-MM-dd")
                    }

                    name?.let { name ->
                        tvTitle.text = name
                    }

                    voteAverage?.let { voteAverage ->
                        val convertRating =
                            voteAverage.toBigDecimal().setScale(1, RoundingMode.UP).toDouble()
                        tvRating.text = convertRating.toFloat().toString()
                    }

                    overview?.let { overview ->
                        tvOverview.text = overview
                    }
                }
            }
            itemView.setOnClickListener {
                onItemClickCallback.onItemClicked(item)
            }
        }
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