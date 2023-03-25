package com.application.zaki.movies.presentation.list.adapter.tvshows

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.application.zaki.movies.databinding.ItemListVerticalBinding
import com.application.zaki.movies.domain.model.genre.GenresItem
import com.application.zaki.movies.domain.model.tvshows.ListTopRatedTvShows
import com.application.zaki.movies.presentation.list.adapter.genres.GenresAdapter
import com.application.zaki.movies.utils.loadImageUrl
import javax.inject.Inject

class TopRatedTvShowsPagingAdapter @Inject constructor() :
    PagingDataAdapter<ListTopRatedTvShows, TopRatedTvShowsPagingAdapter.TopRatedTvShowsPagingViewHolder>(
        DIFF_CALLBACK
    ) {

    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    inner class TopRatedTvShowsPagingViewHolder(private val binding: ItemListVerticalBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ListTopRatedTvShows?) {
            binding.apply {
                item?.apply {
                    posterPath?.let { url ->
                        imgPoster.loadImageUrl(url)
                    }

                    name?.let { name ->
                        tvTitle.text = name
                    }

                    // mapping data genre
                    val genre = ArrayList<GenresItem>()
                    genreIds?.forEach { genreId ->
                        genres?.forEach { genres ->
                            if (genreId == genres?.id) {
                                genre.add(
                                    GenresItem(
                                        name = genres?.name ?: "",
                                        id = genres?.id ?: 0
                                    )
                                )
                            }
                        }
                    }

                    // convert list to adapter
                    val adapter = GenresAdapter(object : GenresAdapter.OnItemClickCallback {
                        override fun onItemClicked(data: GenresItem) {
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
    ): TopRatedTvShowsPagingViewHolder =
        ItemListVerticalBinding.inflate(LayoutInflater.from(parent.context), parent, false).run {
            TopRatedTvShowsPagingViewHolder(this)
        }

    override fun onBindViewHolder(holder: TopRatedTvShowsPagingViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: ListTopRatedTvShows?)
        fun onItemGenreClicked(data: GenresItem)
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ListTopRatedTvShows>() {
            override fun areItemsTheSame(
                oldItem: ListTopRatedTvShows,
                newItem: ListTopRatedTvShows
            ): Boolean = oldItem.id == newItem.id

            override fun areContentsTheSame(
                oldItem: ListTopRatedTvShows,
                newItem: ListTopRatedTvShows
            ): Boolean = oldItem == newItem
        }
    }
}