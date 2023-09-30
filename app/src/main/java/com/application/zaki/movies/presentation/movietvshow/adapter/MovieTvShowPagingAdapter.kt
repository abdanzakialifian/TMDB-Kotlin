package com.application.zaki.movies.presentation.movietvshow.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.application.zaki.movies.databinding.ItemListMovieTvShowPagingBinding
import com.application.zaki.movies.domain.model.MovieTvShow
import com.application.zaki.movies.utils.convertDateText
import com.application.zaki.movies.utils.loadImageUrl
import java.math.RoundingMode
import javax.inject.Inject

class MovieTvShowPagingAdapter @Inject constructor() :
    PagingDataAdapter<MovieTvShow, MovieTvShowPagingAdapter.PopularMoviesPagingViewHolder>(
        DIFF_CALLBACK
    ) {

    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    inner class PopularMoviesPagingViewHolder(private val binding: ItemListMovieTvShowPagingBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: MovieTvShow?) {
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
                        ratingBar.rating = (convertRating / 2).toFloat()
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
        fun onItemClicked(data: MovieTvShow?)
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<MovieTvShow>() {
            override fun areItemsTheSame(
                oldItem: MovieTvShow,
                newItem: MovieTvShow
            ): Boolean = oldItem.id == newItem.id

            override fun areContentsTheSame(
                oldItem: MovieTvShow,
                newItem: MovieTvShow
            ): Boolean = oldItem == newItem
        }
    }
}