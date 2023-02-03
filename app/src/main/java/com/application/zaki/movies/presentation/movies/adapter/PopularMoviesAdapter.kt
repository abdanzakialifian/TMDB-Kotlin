package com.application.zaki.movies.presentation.movies.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.application.zaki.movies.databinding.ItemListHorizontalBinding
import com.application.zaki.movies.domain.model.movies.ListPopularMovies
import com.application.zaki.movies.utils.loadImageUrl
import javax.inject.Inject

class PopularMoviesAdapter @Inject constructor() :
    PagingDataAdapter<ListPopularMovies, PopularMoviesAdapter.PopularMoviesViewHolder>(DIFF_CALLBACK) {

    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    inner class PopularMoviesViewHolder(val binding: ItemListHorizontalBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ListPopularMovies?) {
            item?.posterPath?.let {
                binding.imgHorizontal.loadImageUrl(it)
            }
            itemView.setOnClickListener {
                onItemClickCallback.onItemClicked(item)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PopularMoviesViewHolder =
        ItemListHorizontalBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        ).run {
            PopularMoviesViewHolder(this)
        }

    override fun onBindViewHolder(holder: PopularMoviesViewHolder, position: Int) {
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
        fun onItemClicked(data: ListPopularMovies?)
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ListPopularMovies>() {
            override fun areItemsTheSame(
                oldItem: ListPopularMovies,
                newItem: ListPopularMovies
            ): Boolean = oldItem.id == newItem.id

            override fun areContentsTheSame(
                oldItem: ListPopularMovies,
                newItem: ListPopularMovies
            ): Boolean = oldItem == newItem

        }
    }
}