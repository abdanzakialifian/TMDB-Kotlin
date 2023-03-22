package com.application.zaki.movies.presentation.movies.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.application.zaki.movies.R
import com.application.zaki.movies.databinding.ItemListMoviesBinding
import com.application.zaki.movies.domain.model.movies.MoviesCategoryItem
import com.application.zaki.movies.utils.Movie
import com.application.zaki.movies.utils.gone
import com.application.zaki.movies.utils.visible
import javax.inject.Inject

class MovieCategoryAdapter @Inject constructor() :
    ListAdapter<MoviesCategoryItem, MovieCategoryAdapter.MovieCategoryViewHolder>(DIFF_CALLBACK) {

    private lateinit var movieCategoryItemAdapter: MovieCategoryItemAdapter

    private lateinit var lifecycle: Lifecycle

    private lateinit var onSeeAllClickCallback: OnSeeAllClickCallback

    fun setLifecycle(lifecycle: Lifecycle) {
        this.lifecycle = lifecycle
    }

    fun setOnSeeAllClickCallback(onSeeAllClickCallback: OnSeeAllClickCallback) {
        this.onSeeAllClickCallback = onSeeAllClickCallback
    }

    inner class MovieCategoryViewHolder(private val binding: ItemListMoviesBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: MoviesCategoryItem) {
            binding.apply {
                tvMovies.text = item.categoryTitle
                tvSeeAllMovies.setOnClickListener {
                    Log.d("CEK", item.categoryTitle ?: "")
                    when (item.categoryTitle) {
                        itemView.context.resources.getString(R.string.top_rated_movies) -> onSeeAllClickCallback.onSeeAllClicked(
                            Movie.TOP_RATED_MOVIES
                        )
                        itemView.context.resources.getString(R.string.up_coming_movies) -> onSeeAllClickCallback.onSeeAllClicked(
                            Movie.UP_COMING_MOVIES
                        )
                        itemView.context.resources.getString(R.string.popular_movies) -> onSeeAllClickCallback.onSeeAllClicked(
                            Movie.POPULAR_MOVIES
                        )
                    }
                }
                movieCategoryItemAdapter = MovieCategoryItemAdapter()
                movieCategoryItemAdapter.submitData(
                    lifecycle,
                    item.categories ?: PagingData.empty()
                )
                rvMovies.adapter = movieCategoryItemAdapter
                rvMovies.setHasFixedSize(true)

                movieCategoryItemAdapter.addLoadStateListener { loadState ->
                    when (loadState.refresh) {
                        is LoadState.Loading -> {
                            shimmerMovies.startShimmer()
                            shimmerMovies.visible()
                            layoutMovies.gone()
                        }
                        is LoadState.NotLoading -> {
                            shimmerMovies.stopShimmer()
                            shimmerMovies.gone()
                            layoutMovies.visible()
                        }
                        is LoadState.Error -> {
                            shimmerMovies.stopShimmer()
                            shimmerMovies.gone()
                            layoutMovies.gone()
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

    interface OnSeeAllClickCallback {
        fun onSeeAllClicked(movie: Movie)
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