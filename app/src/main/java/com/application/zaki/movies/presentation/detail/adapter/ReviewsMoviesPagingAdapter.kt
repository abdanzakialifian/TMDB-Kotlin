package com.application.zaki.movies.presentation.detail.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.application.zaki.movies.databinding.ItemListReviewBinding
import com.application.zaki.movies.domain.model.ReviewItem
import com.application.zaki.movies.utils.getInitialName
import javax.inject.Inject

class ReviewsMoviesPagingAdapter @Inject constructor() : PagingDataAdapter<ReviewItem, ReviewsMoviesPagingAdapter.ReviewsPagingViewHolder>(
        DIFF_CALLBACK
    ) {

    inner class ReviewsPagingViewHolder(private val binding: ItemListReviewBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ReviewItem?) {
            binding.apply {
                tvInitialUser.text = item?.author?.getInitialName()
                tvAuthor.text = item?.author
                tvReview.text = item?.content
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ReviewsPagingViewHolder =
        ItemListReviewBinding.inflate(LayoutInflater.from(parent.context), parent, false).run {
            ReviewsPagingViewHolder(this)
        }

    override fun onBindViewHolder(holder: ReviewsPagingViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ReviewItem>() {
            override fun areItemsTheSame(oldItem: ReviewItem, newItem: ReviewItem): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: ReviewItem, newItem: ReviewItem): Boolean =
                oldItem == newItem
        }
    }
}