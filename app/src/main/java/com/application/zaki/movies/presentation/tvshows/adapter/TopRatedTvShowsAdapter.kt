package com.application.zaki.movies.presentation.tvshows.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.application.zaki.movies.databinding.ItemListHorizontalBinding
import com.application.zaki.movies.domain.model.tvshows.ListTvShows
import com.application.zaki.movies.utils.loadImageUrl
import javax.inject.Inject

class TopRatedTvShowsAdapter @Inject constructor() :
    PagingDataAdapter<ListTvShows, TopRatedTvShowsAdapter.TopRatedTvShowsViewHolder>(
        DIFF_CALLBACK
    ) {

    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    inner class TopRatedTvShowsViewHolder(val binding: ItemListHorizontalBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ListTvShows?) {
            item?.posterPath?.let {
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

    interface OnItemClickCallback {
        fun onItemClicked(data: ListTvShows?)
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ListTvShows>() {
            override fun areItemsTheSame(
                oldItem: ListTvShows,
                newItem: ListTvShows
            ): Boolean = oldItem.id == newItem.id

            override fun areContentsTheSame(
                oldItem: ListTvShows,
                newItem: ListTvShows
            ): Boolean = oldItem == newItem
        }
    }
}