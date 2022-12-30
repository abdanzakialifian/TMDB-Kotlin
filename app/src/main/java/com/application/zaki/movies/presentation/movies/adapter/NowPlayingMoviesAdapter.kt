package com.application.zaki.movies.presentation.movies.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.application.zaki.movies.databinding.ItemListSliderBinding
import com.application.zaki.movies.domain.model.movies.ListNowPlayingMovies
import com.application.zaki.movies.utils.loadImageUrl

class NowPlayingMoviesAdapter(private val onItemClickCallback: OnItemClickCallback) :
    ListAdapter<ListNowPlayingMovies, NowPlayingMoviesAdapter.SliderViewHolder>(DIFF_CALLBACK) {

    class SliderViewHolder(private val binding: ItemListSliderBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ListNowPlayingMovies) {
            item.posterPath?.let {
                binding.imgSlider.loadImageUrl(it)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SliderViewHolder =
        ItemListSliderBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        ).run {
            SliderViewHolder(this)
        }

    override fun onBindViewHolder(holder: SliderViewHolder, position: Int) {
        holder.bind(getItem(position))
        holder.itemView.setOnClickListener {
            onItemClickCallback.onItemClicked(getItem(position))
        }
    }

    override fun getItemCount(): Int = if (currentList.size > 8) 8 else currentList.size

    interface OnItemClickCallback {
        fun onItemClicked(data: ListNowPlayingMovies)
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ListNowPlayingMovies>() {
            override fun areItemsTheSame(
                oldItem: ListNowPlayingMovies,
                newItem: ListNowPlayingMovies
            ): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(
                oldItem: ListNowPlayingMovies,
                newItem: ListNowPlayingMovies
            ): Boolean =
                oldItem == newItem
        }
    }
}