package com.application.zaki.movies.presentation.detail.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.application.zaki.movies.databinding.ItemListTrailerBinding
import com.application.zaki.movies.domain.model.Videos
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import javax.inject.Inject

class TrailerAdapter @Inject constructor() :
    ListAdapter<Videos, TrailerAdapter.TrailerViewHolder>(DIFF_CALLBACK) {

    private lateinit var lifecycleOwner: LifecycleOwner

    fun setLifecycleOwner(lifecycleOwner: LifecycleOwner) {
        this.lifecycleOwner = lifecycleOwner
    }

    inner class TrailerViewHolder(private val binding: ItemListTrailerBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Videos) {
            binding.apply {
                if (this@TrailerAdapter::lifecycleOwner.isInitialized) {
                    lifecycleOwner.lifecycle.addObserver(youtubePlayer)
                    youtubePlayer.addYouTubePlayerListener(object :
                        AbstractYouTubePlayerListener() {
                        override fun onReady(youTubePlayer: YouTubePlayer) {
                            super.onReady(youTubePlayer)
                            youTubePlayer.cueVideo(item.key.toString(), 0F)
                        }
                    })
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrailerViewHolder =
        ItemListTrailerBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        ).run {
            TrailerViewHolder(this)
        }

    override fun onBindViewHolder(holder: TrailerViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Videos>() {
            override fun areItemsTheSame(oldItem: Videos, newItem: Videos): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: Videos, newItem: Videos): Boolean =
                oldItem == newItem
        }
    }
}