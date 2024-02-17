package com.application.tmdb.presentation.detail.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.application.tmdb.R
import com.application.tmdb.common.model.ReviewModel
import com.application.tmdb.common.utils.convertDateText
import com.application.tmdb.common.utils.getInitialName
import com.application.tmdb.common.utils.setResizableText
import com.application.tmdb.databinding.ItemListReviewBinding

class ReviewsPagingAdapter :
    PagingDataAdapter<ReviewModel, ReviewsPagingAdapter.ReviewsPagingViewHolder>(
        DIFF_CALLBACK
    ) {

    inner class ReviewsPagingViewHolder(private val binding: ItemListReviewBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ReviewModel?) {
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
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ReviewModel>() {
            override fun areItemsTheSame(oldItem: ReviewModel, newItem: ReviewModel): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: ReviewModel, newItem: ReviewModel): Boolean =
                oldItem == newItem
        }
    }
}