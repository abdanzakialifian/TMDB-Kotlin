package com.application.zaki.movies.presentation.movies.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.application.zaki.movies.databinding.ItemListMoviesBinding
import com.application.zaki.movies.domain.model.movies.MoviesCategoryItem
import com.application.zaki.movies.utils.gone
import com.application.zaki.movies.utils.visible
import javax.inject.Inject

class MovieCategoryAdapter @Inject constructor() :
    ListAdapter<MoviesCategoryItem, MovieCategoryAdapter.MovieCategoryViewHolder>(DIFF_CALLBACK) {

    private lateinit var movieCategoryItemAdapter: MovieCategoryItemAdapter

    private lateinit var lifecycle: Lifecycle

    fun setLifecycle(lifecycle: Lifecycle) {
        this.lifecycle = lifecycle
    }

    inner class MovieCategoryViewHolder(private val binding: ItemListMoviesBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: MoviesCategoryItem) {
            binding.apply {
                tvTitleMovies.text = item.categoryTitle
                movieCategoryItemAdapter = MovieCategoryItemAdapter()
                movieCategoryItemAdapter.submitData(lifecycle, item.categories ?: PagingData.empty())
                rvMovies.adapter = movieCategoryItemAdapter
                rvMovies.visible()
                shimmerMovies.gone()
                rvMovies.setHasFixedSize(true)

                movieCategoryItemAdapter.addLoadStateListener { loadState ->
                    when (loadState.refresh) {
                        is LoadState.Loading -> {
                            binding.apply {
                                shimmerMovies.startShimmer()
                                shimmerMovies.visible()
                                rvMovies.gone()
                            }
                        }
                        is LoadState.NotLoading -> {
                            binding.apply {
                                shimmerMovies.stopShimmer()
                                shimmerMovies.gone()
                                rvMovies.visible()
                            }
                        }
                        is LoadState.Error -> {
                            binding.apply {
                                shimmerMovies.stopShimmer()
                                shimmerMovies.gone()
                                rvMovies.gone()
                            }
                        }
                    }
                }
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