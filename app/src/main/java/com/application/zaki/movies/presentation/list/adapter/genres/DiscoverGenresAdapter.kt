package com.application.zaki.movies.presentation.list.adapter.genres

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.application.zaki.movies.databinding.ItemListDiscoverBinding
import com.application.zaki.movies.domain.model.movies.ResultsItemDiscover
import com.application.zaki.movies.utils.loadImageUrl

class DiscoverGenresAdapter(private val onItemClickCallback: OnItemClickCallback) :
    PagingDataAdapter<ResultsItemDiscover, DiscoverGenresAdapter.DiscoverGenresViewHolder>(
        DIFF_CALLBACK
    ) {
    inner class DiscoverGenresViewHolder(private val binding: ItemListDiscoverBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ResultsItemDiscover?) {
            binding.imgDiscover.loadImageUrl(item?.posterPath ?: "")
            itemView.setOnClickListener {
                onItemClickCallback.onItemClicked(item)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DiscoverGenresViewHolder =
        ItemListDiscoverBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        ).run {
            DiscoverGenresViewHolder(this)
        }

    override fun onBindViewHolder(holder: DiscoverGenresViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: ResultsItemDiscover?)
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ResultsItemDiscover>() {
            override fun areItemsTheSame(
                oldItem: ResultsItemDiscover,
                newItem: ResultsItemDiscover
            ): Boolean = oldItem.id == newItem.id

            override fun areContentsTheSame(
                oldItem: ResultsItemDiscover,
                newItem: ResultsItemDiscover
            ): Boolean = oldItem == newItem
        }
    }
}