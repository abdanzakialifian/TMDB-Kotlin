package com.application.zaki.movies.presentation.tvshows.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.application.zaki.movies.databinding.ItemListSliderBinding
import com.application.zaki.movies.domain.model.tvshows.ListTvShows
import com.application.zaki.movies.utils.loadImageUrl
import javax.inject.Inject

class AiringTodayTvShowsAdapter @Inject constructor() :
    PagingDataAdapter<ListTvShows, AiringTodayTvShowsAdapter.AiringTodayTvShowsViewHolder>(
        DIFF_CALLBACK
    ) {

    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    inner class AiringTodayTvShowsViewHolder(private val binding: ItemListSliderBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ListTvShows?) {
            item?.posterPath?.let {
                binding.imgSlider.loadImageUrl(it)
            }
            itemView.setOnClickListener {
                onItemClickCallback.onItemClicked(item)
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
    }

//    override fun getItemCount(): Int = if (currentList.size > 8) 8 else currentList.size

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