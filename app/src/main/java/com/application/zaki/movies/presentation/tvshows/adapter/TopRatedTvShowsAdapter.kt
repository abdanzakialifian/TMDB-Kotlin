package com.application.zaki.movies.presentation.tvshows.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.application.zaki.movies.databinding.ItemListHorizontalBinding
import com.application.zaki.movies.domain.model.tvshows.ListTopRatedTvShows
import com.application.zaki.movies.utils.loadImageUrl

class TopRatedTvShowsAdapter(private val onItemClickCallback: OnItemClickCallback) :
    ListAdapter<ListTopRatedTvShows, TopRatedTvShowsAdapter.TopRatedTvShowsViewHolder>(DIFF_CALLBACK) {

    inner class TopRatedTvShowsViewHolder(val binding: ItemListHorizontalBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ListTopRatedTvShows) {
            item.posterPath?.let {
                binding.imgHorizontal.loadImageUrl(it)
            }
            itemView.setOnClickListener {
                onItemClickCallback.onItemClicked(item)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TopRatedTvShowsViewHolder =
        ItemListHorizontalBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        ).run {
            TopRatedTvShowsViewHolder(this)
        }

    override fun onBindViewHolder(holder: TopRatedTvShowsViewHolder, position: Int) {
        holder.bind(getItem(position))
        when (position) {
            0 -> {
                val params = ConstraintLayout.LayoutParams(
                    ConstraintLayout.LayoutParams.WRAP_CONTENT,
                    ConstraintLayout.LayoutParams.WRAP_CONTENT
                )
                params.setMargins(30, 0, 15, 0)
                holder.binding.layoutList.layoutParams = params
            }
            7 -> {
                val params = ConstraintLayout.LayoutParams(
                    ConstraintLayout.LayoutParams.WRAP_CONTENT,
                    ConstraintLayout.LayoutParams.WRAP_CONTENT
                )
                params.setMargins(0, 0, 30, 0)
                holder.binding.layoutList.layoutParams = params
            }
            else -> {
                val params = ConstraintLayout.LayoutParams(
                    ConstraintLayout.LayoutParams.WRAP_CONTENT,
                    ConstraintLayout.LayoutParams.WRAP_CONTENT
                )
                params.setMargins(0, 0, 15, 0)
                holder.binding.layoutList.layoutParams = params
            }
        }
    }

    override fun getItemCount(): Int = if (currentList.size > 8) 8 else currentList.size

    interface OnItemClickCallback {
        fun onItemClicked(data: ListTopRatedTvShows)
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ListTopRatedTvShows>() {
            override fun areItemsTheSame(
                oldItem: ListTopRatedTvShows,
                newItem: ListTopRatedTvShows
            ): Boolean = oldItem.id == newItem.id

            override fun areContentsTheSame(
                oldItem: ListTopRatedTvShows,
                newItem: ListTopRatedTvShows
            ): Boolean = oldItem == newItem
        }
    }
}