package com.application.zaki.movies.presentation.movies.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import androidx.paging.PagingData
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.application.zaki.movies.databinding.ItemListMoviesBinding
import com.application.zaki.movies.domain.model.movies.MoviesCategoryItem
import javax.inject.Inject

class MovieCategoryAdapter @Inject constructor() :
    ListAdapter<MoviesCategoryItem, MovieCategoryAdapter.MovieCategoryViewHolder>(DIFF_CALLBACK) {

    @Inject
    lateinit var movieCategoryItemAdapter: MovieCategoryItemAdapter

    private lateinit var lifecycle: Lifecycle

    fun setLifecycle(lifecycle: Lifecycle) {
        this.lifecycle = lifecycle
    }

    inner class MovieCategoryViewHolder(private val binding: ItemListMoviesBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: MoviesCategoryItem) {
            binding.apply {
                tvTitleMovies.text = item.categoryTitle
                movieCategoryItemAdapter.submitData(lifecycle, item.categories ?: PagingData.empty())
                rvMovies.adapter = movieCategoryItemAdapter
                rvMovies.setHasFixedSize(true)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieCategoryViewHolder =
        ItemListMoviesBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        ).run {
            MovieCategoryViewHolder(this)
        }

    override fun onBindViewHolder(holder: MovieCategoryViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<MoviesCategoryItem>() {
            override fun areItemsTheSame(
                oldItem: MoviesCategoryItem,
                newItem: MoviesCategoryItem
            ): Boolean = oldItem.categoryTitle == newItem.categoryTitle

            override fun areContentsTheSame(
                oldItem: MoviesCategoryItem,
                newItem: MoviesCategoryItem
            ): Boolean = oldItem == newItem

        }
    }
}