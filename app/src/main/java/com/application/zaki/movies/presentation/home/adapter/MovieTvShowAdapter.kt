package com.application.zaki.movies.presentation.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.application.zaki.movies.databinding.ItemListCategoryMoviesBinding
import com.application.zaki.movies.domain.model.CategoryItem
import com.application.zaki.movies.domain.model.MovieTvShow
import com.application.zaki.movies.utils.Category
import com.application.zaki.movies.utils.Movie
import com.application.zaki.movies.utils.TvShow
import com.application.zaki.movies.utils.gone
import com.application.zaki.movies.utils.visible
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class MovieTvShowAdapter @Inject constructor() :
    ListAdapter<CategoryItem, MovieTvShowAdapter.MovieCategoryViewHolder>(DIFF_CALLBACK) {

    private lateinit var movieTvShowItemAdapter: MovieTvShowItemAdapter

    private lateinit var onEventClickCallback: OnEventClickCallback

    fun setOnEventClickCallback(onEventClickCallback: OnEventClickCallback) {
        this.onEventClickCallback = onEventClickCallback
    }

    inner class MovieCategoryViewHolder(val binding: ItemListCategoryMoviesBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: CategoryItem) {
            binding.apply {
                if (item.categories == null) {
                    shimmerMovies.startShimmer()
                    shimmerMovies.visible()
                    rvMovies.gone()
                    layoutMovies.gone()
                } else {
                    tvTitlesMovies.text = item.categoryTitle
                    movieTvShowItemAdapter = MovieTvShowItemAdapter()
                    if (this@MovieTvShowAdapter::movieTvShowItemAdapter.isInitialized) {
                        rvMovies.adapter = movieTvShowItemAdapter
                        rvMovies.setHasFixedSize(true)
                        movieTvShowItemAdapter.addLoadStateListener { loadState ->
                            setLoadStatePaging(loadState, binding)
                        }
                        CoroutineScope(Dispatchers.IO).launch {
                            movieTvShowItemAdapter.submitData(item.categories)
                        }
                    }
                    eventListeners(tvSeeAllMovies, item.category, item.movie, item.tvShow)
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

    private fun eventListeners(
        tvSeeAllMovies: TextView,
        category: Category?,
        movie: Movie?,
        tvShow: TvShow?
    ) {
        tvSeeAllMovies.setOnClickListener {
            onEventClickCallback.onSeeAllClicked(category, movie, tvShow)
        }

        movieTvShowItemAdapter.setOnItemClickCallback(object :
            MovieTvShowItemAdapter.OnItemClickCallback {
            override fun onItemClicked(data: MovieTvShow?) {
                if (this@MovieTvShowAdapter::onEventClickCallback.isInitialized) {
                    onEventClickCallback.onItemClicked(data)
                }
            }
        })
    }

    interface OnEventClickCallback {
        fun onSeeAllClicked(category: Category?, movie: Movie?, tvShow: TvShow?)
        fun onItemClicked(data: MovieTvShow?)
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<CategoryItem>() {
            override fun areItemsTheSame(
                oldItem: CategoryItem, newItem: CategoryItem
            ): Boolean = oldItem.categoryId == newItem.categoryId

            override fun areContentsTheSame(
                oldItem: CategoryItem, newItem: CategoryItem
            ): Boolean = oldItem == newItem
        }
    }
}