package com.application.zaki.movies.presentation.movies.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.application.zaki.movies.databinding.ItemListSliderBinding
import com.application.zaki.movies.domain.model.movies.ListMovies
import com.application.zaki.movies.utils.loadImageUrl
import javax.inject.Inject

class NowPlayingMoviesAdapter @Inject constructor() :
    PagingDataAdapter<ListMovies, NowPlayingMoviesAdapter.SliderViewHolder>(DIFF_CALLBACK) {

    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    class SliderViewHolder(private val binding: ItemListSliderBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ListMovies?) {
            item?.posterPath?.let {
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

    interface OnItemClickCallback {
        fun onItemClicked(data: ListMovies?)
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ListMovies>() {
            override fun areItemsTheSame(
                oldItem: ListMovies,
                newItem: ListMovies
            ): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(
                oldItem: ListMovies,
                newItem: ListMovies
            ): Boolean =
                oldItem == newItem
        }
    }
}