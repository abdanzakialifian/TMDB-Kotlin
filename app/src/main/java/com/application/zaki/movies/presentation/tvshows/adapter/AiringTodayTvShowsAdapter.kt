package com.application.zaki.movies.presentation.tvshows.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.application.zaki.movies.databinding.ItemListSliderBinding
import com.application.zaki.movies.domain.model.tvshows.ListAiringTodayTvShows
import com.application.zaki.movies.utils.loadImageUrl
import javax.inject.Inject

class AiringTodayTvShowsAdapter @Inject constructor() :
    ListAdapter<ListAiringTodayTvShows, AiringTodayTvShowsAdapter.AiringTodayTvShowsViewHolder>(
        DIFF_CALLBACK
    ) {

    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    class AiringTodayTvShowsViewHolder(private val binding: ItemListSliderBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ListAiringTodayTvShows) {
            item.posterPath?.let {
                binding.imgSlider.loadImageUrl(it)
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AiringTodayTvShowsViewHolder =
        ItemListSliderBinding.inflate(LayoutInflater.from(parent.context), parent, false).run {
            AiringTodayTvShowsViewHolder(this)
        }

    override fun onBindViewHolder(holder: AiringTodayTvShowsViewHolder, position: Int) {
        holder.bind(getItem(position))
        holder.itemView.setOnClickListener {
            onItemClickCallback.onItemClicked(getItem(position))
        }
    }

    override fun getItemCount(): Int = if (currentList.size > 8) 8 else currentList.size

    interface OnItemClickCallback {
        fun onItemClicked(data: ListAiringTodayTvShows)
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ListAiringTodayTvShows>() {
            override fun areItemsTheSame(
                oldItem: ListAiringTodayTvShows,
                newItem: ListAiringTodayTvShows
            ): Boolean = oldItem.id == newItem.id

            override fun areContentsTheSame(
                oldItem: ListAiringTodayTvShows,
                newItem: ListAiringTodayTvShows
            ): Boolean = oldItem == newItem
        }
    }
}