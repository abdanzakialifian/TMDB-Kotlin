package com.application.zaki.movies.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.application.zaki.movies.databinding.ItemListMovieTvShowBinding
import com.application.zaki.movies.domain.model.CategoryModel
import com.application.zaki.movies.domain.model.MovieTvShowModel
import com.application.zaki.movies.utils.Category
import com.application.zaki.movies.utils.Movie
import com.application.zaki.movies.utils.TvShow
import com.application.zaki.movies.utils.gone
import com.application.zaki.movies.utils.visible
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MovieTvShowAdapter :
    ListAdapter<CategoryModel, MovieTvShowAdapter.MovieCategoryViewHolder>(DIFF_CALLBACK) {

    private lateinit var movieTvShowItemPagingAdapter: MovieTvShowItemPagingAdapter

    private lateinit var onEventClickCallback: OnEventClickCallback

    fun setOnEventClickCallback(onEventClickCallback: OnEventClickCallback) {
        this.onEventClickCallback = onEventClickCallback
    }

    inner class MovieCategoryViewHolder(val binding: ItemListMovieTvShowBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: CategoryModel) {
            binding.apply {
                tvTitleMovieTvShow.text = item.categoryTitle
                movieTvShowItemPagingAdapter = MovieTvShowItemPagingAdapter()
                if (this@MovieTvShowAdapter::movieTvShowItemPagingAdapter.isInitialized) {
                    CoroutineScope(Dispatchers.IO).launch {
                        movieTvShowItemPagingAdapter.submitData(item.categories ?: PagingData.empty())
                    }
                    rvMovieTvShow.adapter = movieTvShowItemPagingAdapter
                    rvMovieTvShow.setHasFixedSize(true)
                    movieTvShowItemPagingAdapter.addLoadStateListener { loadState ->
                        setLoadStatePaging(loadState, binding)
                    }
                }
                eventListeners(tvSeeAllMovieTvShow, item.category, item.movie, item.tvShow)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieCategoryViewHolder =
        ItemListMovieTvShowBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        ).run {
            MovieCategoryViewHolder(this)
        }

    override fun onBindViewHolder(holder: MovieCategoryViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    private fun setLoadStatePaging(
        loadState: CombinedLoadStates,
        binding: ItemListMovieTvShowBinding
    ) {
        binding.apply {
            when (loadState.refresh) {
                is LoadState.Loading -> {
                    shimmerMovieTvShow.startShimmer()
                    shimmerMovieTvShow.visible()
                    rvMovieTvShow.gone()
                    layoutMovieTvShow.gone()
                }

                is LoadState.NotLoading -> {
                    shimmerMovieTvShow.stopShimmer()
                    shimmerMovieTvShow.gone()
                    rvMovieTvShow.visible()
                    layoutMovieTvShow.visible()
                }

                is LoadState.Error -> {
                    shimmerMovieTvShow.stopShimmer()
                    shimmerMovieTvShow.gone()
                    rvMovieTvShow.gone()
                    layoutMovieTvShow.gone()
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

        movieTvShowItemPagingAdapter.setOnItemClickCallback(object :
            MovieTvShowItemPagingAdapter.OnItemClickCallback {
            override fun onItemClicked(data: MovieTvShowModel?) {
                if (this@MovieTvShowAdapter::onEventClickCallback.isInitialized) {
                    onEventClickCallback.onItemClicked(data)
                }
            }
        })
    }

    interface OnEventClickCallback {
        fun onSeeAllClicked(category: Category?, movie: Movie?, tvShow: TvShow?)
        fun onItemClicked(data: MovieTvShowModel?)
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<CategoryModel>() {
            override fun areItemsTheSame(
                oldItem: CategoryModel, newItem: CategoryModel
            ): Boolean = oldItem.categoryId == newItem.categoryId

            override fun areContentsTheSame(
                oldItem: CategoryModel, newItem: CategoryModel
            ): Boolean = oldItem == newItem
        }
    }
}