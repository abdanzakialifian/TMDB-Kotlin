package com.application.zaki.movies.presentation.list.adapter.movies

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.application.zaki.movies.databinding.ItemListVerticalBinding
import com.application.zaki.movies.domain.model.genre.Genre
import com.application.zaki.movies.domain.model.movies.ListUpComingMovies
import com.application.zaki.movies.presentation.detail.view.DetailFragment
import com.application.zaki.movies.presentation.list.adapter.genres.GenresAdapter
import com.application.zaki.movies.presentation.list.view.ListFragmentDirections
import com.application.zaki.movies.utils.loadImageUrl

class UpComingMoviesPagingAdapter(private val onItemClickCallback: OnItemClickCallback) :
    PagingDataAdapter<ListUpComingMovies, UpComingMoviesPagingAdapter.UpComingMoviesPagingViewHolder>(
        DIFF_CALLBACK
    ) {
    inner class UpComingMoviesPagingViewHolder(private val binding: ItemListVerticalBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ListUpComingMovies?) {
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
    ): UpComingMoviesPagingViewHolder = ItemListVerticalBinding.inflate(
        LayoutInflater.from(parent.context), parent, false
    ).run {
        UpComingMoviesPagingViewHolder(this)
    }

    override fun onBindViewHolder(
        holder: UpComingMoviesPagingViewHolder,
        position: Int
    ) {
        holder.bind(getItem(position))
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: ListUpComingMovies?)
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ListUpComingMovies>() {
            override fun areItemsTheSame(
                oldItem: ListUpComingMovies,
                newItem: ListUpComingMovies
            ): Boolean = oldItem.id == newItem.id

            override fun areContentsTheSame(
                oldItem: ListUpComingMovies,
                newItem: ListUpComingMovies
            ): Boolean = oldItem == newItem
        }
    }
}