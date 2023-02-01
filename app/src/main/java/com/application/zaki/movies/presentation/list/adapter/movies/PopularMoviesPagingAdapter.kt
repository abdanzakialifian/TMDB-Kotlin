package com.application.zaki.movies.presentation.list.adapter.movies

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.application.zaki.movies.databinding.ItemListVerticalBinding
import com.application.zaki.movies.domain.model.genre.Genre
import com.application.zaki.movies.domain.model.movies.ListPopularMovies
import com.application.zaki.movies.presentation.detail.view.DetailFragment
import com.application.zaki.movies.presentation.list.adapter.genres.GenresAdapter
import com.application.zaki.movies.presentation.list.view.ListFragmentDirections
import com.application.zaki.movies.utils.loadImageUrl

class PopularMoviesPagingAdapter(private val onItemClickCallback: OnItemClickCallback) :
    PagingDataAdapter<ListPopularMovies, PopularMoviesPagingAdapter.PopularMoviesPagingViewHolder>(
        DIFF_CALLBACK
    ) {

    inner class PopularMoviesPagingViewHolder(private val binding: ItemListVerticalBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ListPopularMovies?) {
            binding.apply {
                item?.apply {
                    posterPath?.let { url ->
                        imgPoster.loadImageUrl(url)
                    }

                    title?.let { title ->
                        tvTitle.text = title
                    }

                    // mapping data genre
                    val genre = ArrayList<Genre>()
                    genreIds?.forEach { genreId ->
                        genres?.map {
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
                            navigateToDetailFragment.genreId = data.genreId ?: 0
                            navigateToDetailFragment.intentFrom = DetailFragment.INTENT_FROM_MOVIE
                            navigateToDetailFragment.genreName = data.genreName ?: ""
                            itemView.findNavController().navigate(navigateToDetailFragment)
                        }
                    })
                    adapter.submitList(genre)
                    rvGenre.adapter = adapter
                    rvGenre.setHasFixedSize(true)

                    tvRate.text = voteAverage.toString()
                }
            }
            itemView.setOnClickListener {
                onItemClickCallback.onItemClicked(item)
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PopularMoviesPagingViewHolder =
        ItemListVerticalBinding.inflate(LayoutInflater.from(parent.context), parent, false).run {
            PopularMoviesPagingViewHolder(this)
        }

    override fun onBindViewHolder(holder: PopularMoviesPagingViewHolder, position: Int) {
        holder.bind(getItem(position))
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