package com.application.zaki.movies.presentation.listmovietvshow.adapter.genres

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.application.zaki.movies.databinding.ItemListDiscoverBinding
import com.application.zaki.movies.domain.model.DiscoverItem
import com.application.zaki.movies.utils.loadImageUrl
import javax.inject.Inject

class DiscoverGenresAdapter @Inject constructor() :
    PagingDataAdapter<DiscoverItem, DiscoverGenresAdapter.DiscoverGenresViewHolder>(
        DIFF_CALLBACK
    ) {

    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }
    inner class DiscoverGenresViewHolder(private val binding: ItemListDiscoverBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: DiscoverItem?) {
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
        fun onItemClicked(data: DiscoverItem?)
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<DiscoverItem>() {
            override fun areItemsTheSame(
                oldItem: DiscoverItem,
                newItem: DiscoverItem
            ): Boolean = oldItem.id == newItem.id

            override fun areContentsTheSame(
                oldItem: DiscoverItem,
                newItem: DiscoverItem
            ): Boolean = oldItem == newItem
        }
    }
}