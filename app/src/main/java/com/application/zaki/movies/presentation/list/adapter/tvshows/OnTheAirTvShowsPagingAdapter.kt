package com.application.zaki.movies.presentation.list.adapter.tvshows

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.application.zaki.movies.databinding.ItemListVerticalBinding
import com.application.zaki.movies.domain.model.genre.Genre
import com.application.zaki.movies.domain.model.tvshows.ListOnTheAirTvShows
import com.application.zaki.movies.presentation.list.adapter.genres.GenresAdapter
import com.application.zaki.movies.utils.loadImageUrl
import javax.inject.Inject

class OnTheAirTvShowsPagingAdapter @Inject constructor() :
    PagingDataAdapter<ListOnTheAirTvShows, OnTheAirTvShowsPagingAdapter.OnTheAirTvShowsPagingViewHolder>(
        DIFF_CALLBACK
    ) {

    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    inner class OnTheAirTvShowsPagingViewHolder(private val binding: ItemListVerticalBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ListOnTheAirTvShows?) {
            binding.apply {
                item?.apply {
                    posterPath?.let { url ->
                        imgPoster.loadImageUrl(url)
                    }

                    name?.let { name ->
                        tvTitle.text = name
                    }

                    // mapping data genre
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
                            onItemClickCallback.onItemGenreClicked(data)
                        }
                    })
                    adapter.submitList(genre)
                    rvGenre.adapter = adapter

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
    ): OnTheAirTvShowsPagingViewHolder =
        ItemListVerticalBinding.inflate(LayoutInflater.from(parent.context), parent, false).run {
            OnTheAirTvShowsPagingViewHolder(this)
        }

    override fun onBindViewHolder(holder: OnTheAirTvShowsPagingViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: ListOnTheAirTvShows?)
        fun onItemGenreClicked(data: Genre)
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ListOnTheAirTvShows>() {
            override fun areItemsTheSame(
                oldItem: ListOnTheAirTvShows,
                newItem: ListOnTheAirTvShows
            ): Boolean = oldItem.id == newItem.id

            override fun areContentsTheSame(
                oldItem: ListOnTheAirTvShows,
                newItem: ListOnTheAirTvShows
            ): Boolean = oldItem == newItem
        }
    }
}