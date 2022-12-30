package com.application.zaki.movies.presentation.list.adapter.movies

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.findNavController
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.application.zaki.movies.databinding.ItemListVerticalBinding
import com.application.zaki.movies.domain.model.genre.Genre
import com.application.zaki.movies.domain.model.movies.ListTopRatedMovies
import com.application.zaki.movies.presentation.detail.view.DetailFragment
import com.application.zaki.movies.presentation.list.adapter.genres.GenresAdapter
import com.application.zaki.movies.presentation.list.view.ListFragmentDirections
import com.application.zaki.movies.utils.loadImageUrl

class TopRatedMoviesPagingAdapter(private val onItemClickCallback: OnItemClickCallback) :
    PagingDataAdapter<ListTopRatedMovies, TopRatedMoviesPagingAdapter.TopRatedMoviesPagingViewHolder>(
        DIFF_CALLBACK
    ) {
    inner class TopRatedMoviesPagingViewHolder(private val binding: ItemListVerticalBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ListTopRatedMovies?) {
            binding.apply {
                item?.apply {
                    posterPath?.let { url ->
                        imgPoster.loadImageUrl(url)
                    }

                    title?.let { title ->
                        tvTitle.text = title
                    }

                    val genre = ArrayList<Genre>()
                    genreIds?.forEach { genreId ->
                        genres?.forEach {
                            if (genreId == it?.id) {
                                genre.add(Genre(it?.id ?: 0, it?.name ?: ""))
                            }
                        }
                    }

                    // convert list to adapter
                    val adapter = GenresAdapter(object : GenresAdapter.OnItemClickCallback {
                        override fun onItemClicked(data: Genre) {
                            val navigateToDetailFragment =
                                ListFragmentDirections.actionListFragmentToListDiscoverFragment()
                            navigateToDetailFragment.genreId = data?.genreId ?: 0
                            navigateToDetailFragment.intentFrom = DetailFragment.INTENT_FROM_MOVIE
                            navigateToDetailFragment.genreName = data.genreName ?: ""
                            itemView.findNavController().navigate(navigateToDetailFragment)
                        }
                    })
                    adapter.submitList(genre)
                    rvGenre.adapter = adapter
                    rvGenre.setHasFixedSize(true)

                    tvRate.text = voteAverage?.toString()
                }
                itemView.setOnClickListener {
                    onItemClickCallback.onItemClicked(item)
                }
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TopRatedMoviesPagingViewHolder =
        ItemListVerticalBinding.inflate(LayoutInflater.from(parent.context), parent, false).run {
            TopRatedMoviesPagingViewHolder(this)
        }

    override fun onBindViewHolder(holder: TopRatedMoviesPagingViewHolder, position: Int) {
        holder.bind(getItem(position))
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