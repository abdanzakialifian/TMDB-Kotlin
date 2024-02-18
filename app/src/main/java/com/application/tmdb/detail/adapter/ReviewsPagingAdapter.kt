package com.application.tmdb.detail.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.application.tmdb.common.R
import com.application.tmdb.common.model.ReviewModel
import com.application.tmdb.common.utils.Constant
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
                    item?.createdAt?.convertDateText(
                        Constant.FORMAT_DD_MMM_YYYY,
                        Constant.FORMAT_YYYY_MM_DD,
                    )
                )
                tvReview.setResizableText(item?.content.orEmpty(), 5, true, layoutText)
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