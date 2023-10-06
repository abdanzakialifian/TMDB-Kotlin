package com.application.zaki.movies.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.application.zaki.movies.databinding.ItemListMovieTvShowSliderBinding
import com.application.zaki.movies.domain.model.MovieTvShowModel
import com.application.zaki.movies.utils.loadBackdropImageUrl

class MovieTvShowSliderPagingAdapter :
    PagingDataAdapter<MovieTvShowModel, MovieTvShowSliderPagingAdapter.SliderViewHolder>(
        DIFF_CALLBACK
    ) {

    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    inner class SliderViewHolder(private val binding: ItemListMovieTvShowSliderBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: MovieTvShowModel?) {
            item?.backdropPath?.let {
                binding.imgSlider.loadBackdropImageUrl(it)
            }
            itemView.setOnClickListener {
                onItemClickCallback.onItemClicked(item)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SliderViewHolder =
        ItemListMovieTvShowSliderBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        ).run {
            SliderViewHolder(this)
        }

    override fun onBindViewHolder(holder: SliderViewHolder, position: Int) {
        val newListRandom = snapshot().items.shuffled()
        holder.bind(newListRandom[position])
    }

    override fun getItemCount(): Int = 5

    interface OnItemClickCallback {
        fun onItemClicked(data: MovieTvShowModel?)
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<MovieTvShowModel>() {
            override fun areItemsTheSame(
                oldItem: MovieTvShowModel,
                newItem: MovieTvShowModel
            ): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(
                oldItem: MovieTvShowModel,
                newItem: MovieTvShowModel
            ): Boolean =
                oldItem == newItem
        }
    }
}