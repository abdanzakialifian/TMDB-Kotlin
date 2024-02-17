package com.application.tmdb.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.application.tmdb.databinding.ItemListMovieTvShowItemBinding
import com.application.tmdb.core.domain.model.MovieTvShowModel
import com.application.tmdb.utils.loadImageUrl

class MovieTvShowItemPagingAdapter :
    PagingDataAdapter<MovieTvShowModel, MovieTvShowItemPagingAdapter.MovieItemViewHolder>(
        DIFF_CALLBACK
    ) {

    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    inner class MovieItemViewHolder(val binding: ItemListMovieTvShowItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: MovieTvShowModel?) {
            item?.posterPath?.let {
                binding.imgHorizontal.loadImageUrl(it)
            }
            if (this@MovieTvShowItemPagingAdapter::onItemClickCallback.isInitialized) {
                itemView.setOnClickListener {
                    onItemClickCallback.onItemClicked(item)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieItemViewHolder =
        ItemListMovieTvShowItemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        ).run {
            MovieItemViewHolder(this)
        }

    override fun onBindViewHolder(holder: MovieItemViewHolder, position: Int) {
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