package com.application.zaki.movies.presentation.list.adapter.movies

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.application.zaki.movies.databinding.ItemListVerticalBinding
import com.application.zaki.movies.domain.model.genre.Genre
import com.application.zaki.movies.domain.model.movies.ListPopularMovies
import com.application.zaki.movies.presentation.list.adapter.genres.GenresAdapter
import com.application.zaki.movies.utils.loadImageUrl
import javax.inject.Inject

class PopularMoviesPagingAdapter @Inject constructor() :
    PagingDataAdapter<ListPopularMovies, PopularMoviesPagingAdapter.PopularMoviesPagingViewHolder>(
        DIFF_CALLBACK
    ) {

    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

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
                            onItemClickCallback.onItemGenreClicked(data)
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
        fun onItemGenreClicked(data: Genre)
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