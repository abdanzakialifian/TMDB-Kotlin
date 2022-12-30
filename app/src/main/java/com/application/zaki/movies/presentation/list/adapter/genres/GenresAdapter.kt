package com.application.zaki.movies.presentation.list.adapter.genres

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.application.zaki.movies.databinding.ItemListGenreBinding
import com.application.zaki.movies.domain.model.genre.Genre

class GenresAdapter(private val onItemClickCallback: OnItemClickCallback) :
    ListAdapter<Genre, GenresAdapter.GenresViewHolder>(DIFF_CALLBACK) {

    inner class GenresViewHolder(private val binding: ItemListGenreBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Genre) {
            binding.tvGenre.text = item.genreName
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
        fun onItemClicked(data: Genre)
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Genre>() {
            override fun areItemsTheSame(oldItem: Genre, newItem: Genre): Boolean =
                oldItem.genreId == newItem.genreId

            override fun areContentsTheSame(oldItem: Genre, newItem: Genre): Boolean =
                oldItem == newItem
        }
    }
}