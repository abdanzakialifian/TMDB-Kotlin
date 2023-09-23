package com.application.zaki.movies.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.application.zaki.movies.databinding.ItemListCategoryMoviesBinding
import com.application.zaki.movies.domain.model.MovieTvShow
import com.application.zaki.movies.domain.model.MoviesCategoryItem
import com.application.zaki.movies.utils.Movie
import com.application.zaki.movies.utils.gone
import com.application.zaki.movies.utils.visible
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class MovieAdapter @Inject constructor() :
    ListAdapter<MoviesCategoryItem, MovieAdapter.MovieCategoryViewHolder>(DIFF_CALLBACK) {

    private lateinit var movieItemAdapter: MovieItemAdapter

    private lateinit var onEventClickCallback: OnEventClickCallback

    fun setOnEventClickCallback(onEventClickCallback: OnEventClickCallback) {
        this.onEventClickCallback = onEventClickCallback
    }

    inner class MovieCategoryViewHolder(val binding: ItemListCategoryMoviesBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: MoviesCategoryItem) {
            binding.apply {
                if (item.categories == null) {
                    shimmerMovies.startShimmer()
                    shimmerMovies.visible()
                    rvMovies.gone()
                    layoutMovies.gone()
                } else {
                    tvTitlesMovies.text = item.categoryTitle
                    movieItemAdapter = MovieItemAdapter()
                    if (this@MovieAdapter::movieItemAdapter.isInitialized) {
                        rvMovies.adapter = movieItemAdapter
                        rvMovies.setHasFixedSize(true)
                        movieItemAdapter.addLoadStateListener { loadState ->
                            setLoadStatePaging(loadState, binding)
                        }
                        CoroutineScope(Dispatchers.IO).launch {
                            movieItemAdapter.submitData(item.categories ?: PagingData.empty())
                        }
                    }
                    eventListeners(tvSeeAllMovies, item.movie)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieCategoryViewHolder =
        ItemListCategoryMoviesBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        ).run {
            MovieCategoryViewHolder(this)
        }

    override fun onBindViewHolder(holder: MovieCategoryViewHolder, position: Int) {
        holder.bind(getItem(position))
        if (position != 0) {
            val params = ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.WRAP_CONTENT,
                ConstraintLayout.LayoutParams.WRAP_CONTENT
            )
            params.setMargins(0, 60, 0, 0)
            holder.binding.layoutCategory.layoutParams = params
        }
    }

    private fun setLoadStatePaging(
        loadState: CombinedLoadStates,
        binding: ItemListCategoryMoviesBinding
    ) {
        binding.apply {
            when (loadState.refresh) {
                is LoadState.Loading -> {
                    shimmerMovies.startShimmer()
                    shimmerMovies.visible()
                    rvMovies.gone()
                    layoutMovies.gone()
                }

                is LoadState.NotLoading -> {
                    shimmerMovies.stopShimmer()
                    shimmerMovies.gone()
                    rvMovies.visible()
                    layoutMovies.visible()
                }

                is LoadState.Error -> {
                    shimmerMovies.stopShimmer()
                    shimmerMovies.gone()
                    rvMovies.gone()
                    layoutMovies.gone()
                }
            }
        }
    }

    private fun eventListeners(tvSeeAllMovies: TextView, movie: Movie?) {
        tvSeeAllMovies.setOnClickListener {
            onEventClickCallback.onSeeAllClicked(movie)
        }

        movieItemAdapter.setOnItemClickCallback(object : MovieItemAdapter.OnItemClickCallback {
            override fun onItemClicked(data: MovieTvShow?) {
                if (this@MovieAdapter::onEventClickCallback.isInitialized) {
                    onEventClickCallback.onItemClicked(data)
                }
            }
        })
    }

    interface OnEventClickCallback {
        fun onSeeAllClicked(movie: Movie?)
        fun onItemClicked(data: MovieTvShow?)
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<MoviesCategoryItem>() {
            override fun areItemsTheSame(
                oldItem: MoviesCategoryItem, newItem: MoviesCategoryItem
            ): Boolean = oldItem.categoryId == newItem.categoryId

            override fun areContentsTheSame(
                oldItem: MoviesCategoryItem,
                newItem: MoviesCategoryItem
            ): Boolean = oldItem == newItem
        }
    }
}