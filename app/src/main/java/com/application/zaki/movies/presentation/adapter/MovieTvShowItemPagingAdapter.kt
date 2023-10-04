package com.application.zaki.movies.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.application.zaki.movies.databinding.ItemListMovieTvShowItemBinding
import com.application.zaki.movies.domain.model.MovieTvShow
import com.application.zaki.movies.utils.loadImageUrl

class MovieTvShowItemPagingAdapter :
    PagingDataAdapter<MovieTvShow, MovieTvShowItemPagingAdapter.MovieItemViewHolder>(DIFF_CALLBACK) {

    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    inner class MovieItemViewHolder(val binding: ItemListMovieTvShowItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: MovieTvShow?) {
            item?.posterPath?.let {
                binding.imgHorizontal.loadImageUrl(it)
            }
            itemView.setOnClickListener {
                onItemClickCallback.onItemClicked(item)
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