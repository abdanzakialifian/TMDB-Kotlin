package com.application.zaki.movies.presentation.movies.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.application.zaki.movies.databinding.ItemListHorizontalBinding
import com.application.zaki.movies.domain.model.movies.ListTopRatedMovies
import com.application.zaki.movies.utils.loadImageUrl
import javax.inject.Inject

class TopRatedMoviesAdapter @Inject constructor() :
    PagingDataAdapter<ListTopRatedMovies, TopRatedMoviesAdapter.TopRatedMoviesViewHolder>(DIFF_CALLBACK) {

    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    inner class TopRatedMoviesViewHolder(val binding: ItemListHorizontalBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ListTopRatedMovies?) {
            item?.posterPath?.let {
                binding.imgHorizontal.loadImageUrl(it)
            }
            itemView.setOnClickListener {
                onItemClickCallback.onItemClicked(item)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TopRatedMoviesViewHolder =
        ItemListHorizontalBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        ).run {
            TopRatedMoviesViewHolder(this)
        }

    override fun onBindViewHolder(holder: TopRatedMoviesViewHolder, position: Int) {
        holder.bind(getItem(position))
        when (position) {
            0 -> {
                val params = ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.WRAP_CONTENT, ConstraintLayout.LayoutParams.WRAP_CONTENT)
                params.setMargins(30, 0, 15,0)
                holder.binding.layoutList.layoutParams = params
            }
            7 -> {
                val params = ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.WRAP_CONTENT, ConstraintLayout.LayoutParams.WRAP_CONTENT)
                params.setMargins(0, 0, 30,0)
                holder.binding.layoutList.layoutParams = params
            }
            else -> {
                val params = ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.WRAP_CONTENT, ConstraintLayout.LayoutParams.WRAP_CONTENT)
                params.setMargins(0, 0, 15,0)
                holder.binding.layoutList.layoutParams = params
            }
        }
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: ListTopRatedMovies?)
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ListTopRatedMovies>() {
            override fun areItemsTheSame(
                oldItem: ListTopRatedMovies,
                newItem: ListTopRatedMovies
            ): Boolean = oldItem.id == newItem.id

            override fun areContentsTheSame(
                oldItem: ListTopRatedMovies,
                newItem: ListTopRatedMovies
            ): Boolean = oldItem == newItem
        }
    }
}