package com.application.zaki.movies.presentation.detail.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.application.zaki.movies.R
import com.application.zaki.movies.databinding.ItemListReviewBinding
import com.application.zaki.movies.domain.model.ReviewItem
import com.application.zaki.movies.utils.convertDateText
import com.application.zaki.movies.utils.getInitialName
import com.application.zaki.movies.utils.setResizableText
import javax.inject.Inject

class ReviewsPagingAdapter @Inject constructor() : PagingDataAdapter<ReviewItem, ReviewsPagingAdapter.ReviewsPagingViewHolder>(
        DIFF_CALLBACK
    ) {

    inner class ReviewsPagingViewHolder(private val binding: ItemListReviewBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ReviewItem?) {
            binding.apply {
                tvInitialUser.text = item?.author?.getInitialName()
                tvUserName.text = item?.author
                tvCreatedAt.text = itemView.resources.getString(
                    R.string.on,
                    item?.createdAt?.convertDateText("dd MMM yyyy", "yyyy-MM-dd")
                )
                tvReview.setResizableText(item?.content ?: "", 5, true, layoutText)
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