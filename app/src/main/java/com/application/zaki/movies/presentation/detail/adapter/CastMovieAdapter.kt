package com.application.zaki.movies.presentation.detail.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.application.zaki.movies.databinding.ItemListCastBinding
import com.application.zaki.movies.domain.model.movies.CastItem
import com.application.zaki.movies.utils.loadImageUrl

class CastMovieAdapter : ListAdapter<CastItem, CastMovieAdapter.CastViewHolder>(DIFF_CALLBACK) {
    inner class CastViewHolder(private val binding: ItemListCastBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: CastItem) {
            binding.apply {
                imgCast.loadImageUrl(item.profilePath ?: "")
                tvCast.text = item.name
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CastViewHolder =
        ItemListCastBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        ).run {
            CastViewHolder(this)
        }

    override fun onBindViewHolder(holder: CastViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<CastItem>() {
            override fun areItemsTheSame(oldItem: CastItem, newItem: CastItem): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: CastItem, newItem: CastItem): Boolean =
                oldItem == newItem
        }
    }
}