package com.application.zaki.movies.presentation.movies.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.Lifecycle
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.application.zaki.movies.R
import com.application.zaki.movies.databinding.ItemListCategoryMoviesBinding
import com.application.zaki.movies.domain.model.movies.ListMovies
import com.application.zaki.movies.domain.model.movies.MoviesCategoryItem
import com.application.zaki.movies.utils.Movie
import com.application.zaki.movies.utils.gone
import com.application.zaki.movies.utils.visible
import javax.inject.Inject

class MovieAdapter @Inject constructor() :
    ListAdapter<MoviesCategoryItem, MovieAdapter.MovieCategoryViewHolder>(DIFF_CALLBACK) {

    private lateinit var movieItemAdapter: MovieItemAdapter

    private lateinit var lifecycle: Lifecycle

    private lateinit var onEventClickCallback: OnEventClickCallback

    fun setLifecycle(lifecycle: Lifecycle) {
        this.lifecycle = lifecycle
    }

    fun setOnSeeAllClickCallback(onEventClickCallback: OnEventClickCallback) {
        this.onEventClickCallback = onEventClickCallback
    }

    inner class MovieCategoryViewHolder(val binding: ItemListCategoryMoviesBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: MoviesCategoryItem) {
            binding.apply {
                tvTitlesMovies.text = item.categoryTitle
                tvSeeAllMovies.setOnClickListener {
                    setSeeAllClick(item.categoryTitle, itemView.context)
                }

                movieItemAdapter = MovieItemAdapter()
                setMovieItemClick()
                movieItemAdapter.submitData(
                    lifecycle,
                    item.categories ?: PagingData.empty()
                )
                rvMovies.adapter = movieItemAdapter
                rvMovies.setHasFixedSize(true)
                movieItemAdapter.addLoadStateListener { loadState ->
                    setLoadStatePaging(loadState, binding)
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

    private fun setMovieItemClick() {
        movieItemAdapter.setOnItemClickCallback(object : MovieItemAdapter.OnItemClickCallback {
            override fun onItemClicked(data: ListMovies?) {
                onEventClickCallback.onItemClicked(data)
            }
        })
    }

    private fun setSeeAllClick(categoryTitle: String?, context: Context) {
        when (categoryTitle) {
            context.resources.getString(R.string.top_rated_movies) -> onEventClickCallback.onSeeAllClicked(
                Movie.TOP_RATED_MOVIES
            )
            context.resources.getString(R.string.up_coming_movies) -> onEventClickCallback.onSeeAllClicked(
                Movie.UP_COMING_MOVIES
            )
            context.resources.getString(R.string.popular_movies) -> onEventClickCallback.onSeeAllClicked(
                Movie.POPULAR_MOVIES
            )
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

    interface OnEventClickCallback {
        fun onSeeAllClicked(movie: Movie)
        fun onItemClicked(data: ListMovies?)
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