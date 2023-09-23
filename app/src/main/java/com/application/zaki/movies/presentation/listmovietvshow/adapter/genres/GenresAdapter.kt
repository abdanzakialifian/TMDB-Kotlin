package com.application.zaki.movies.presentation.listmovietvshow.adapter.genres

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.application.zaki.movies.databinding.ItemListGenreBinding
import com.application.zaki.movies.domain.model.GenresItem

class GenresAdapter(private val onItemClickCallback: OnItemClickCallback) :
    ListAdapter<GenresItem, GenresAdapter.GenresViewHolder>(DIFF_CALLBACK) {

    inner class GenresViewHolder(private val binding: ItemListGenreBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: GenresItem) {
            binding.tvGenre.text = item.name
            itemView.setOnClickListener {
                onItemClickCallback.onItemClicked(item)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GenresViewHolder =
        ItemListGenreBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        ).run {
            GenresViewHolder(this)
        }

    override fun onBindViewHolder(holder: GenresViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: GenresItem)
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<GenresItem>() {
            override fun areItemsTheSame(oldItem: GenresItem, newItem: GenresItem): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: GenresItem, newItem: GenresItem): Boolean =
                oldItem == newItem
        }
    }
}