package com.application.zaki.movies.presentation.tvshows.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.application.zaki.movies.databinding.ItemListHorizontalBinding
import com.application.zaki.movies.domain.model.tvshows.ListOnTheAirTvShows
import com.application.zaki.movies.utils.loadImageUrl
import javax.inject.Inject

class OnTheAirTvShowsAdapter @Inject constructor() :
    PagingDataAdapter<ListOnTheAirTvShows, OnTheAirTvShowsAdapter.OnTheAirTvShowsViewHolder>(
        DIFF_CALLBACK
    ) {

    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    inner class OnTheAirTvShowsViewHolder(val binding: ItemListHorizontalBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ListOnTheAirTvShows?) {
            item?.posterPath?.let {
                binding.imgHorizontal.loadImageUrl(it)
            }
            itemView.setOnClickListener {
                onItemClickCallback.onItemClicked(item)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OnTheAirTvShowsViewHolder =
        ItemListHorizontalBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        ).run {
            OnTheAirTvShowsViewHolder(this)
        }

    override fun onBindViewHolder(holder: OnTheAirTvShowsViewHolder, position: Int) {
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
        fun onItemClicked(data: ListOnTheAirTvShows?)
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ListOnTheAirTvShows>() {
            override fun areItemsTheSame(
                oldItem: ListOnTheAirTvShows,
                newItem: ListOnTheAirTvShows
            ): Boolean = oldItem.id == newItem.id

            override fun areContentsTheSame(
                oldItem: ListOnTheAirTvShows,
                newItem: ListOnTheAirTvShows
            ): Boolean = oldItem == newItem
        }
    }
}