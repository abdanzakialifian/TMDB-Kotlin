package com.application.zaki.movies.presentation.detail.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.application.zaki.movies.databinding.ItemListReviewBinding
import com.application.zaki.movies.domain.model.movies.ReviewItem
import com.application.zaki.movies.utils.getInitialName

class ReviewsAdapter : RecyclerView.Adapter<ReviewsAdapter.ReviewsPagingViewHolder>() {
    private var listReview = ArrayList<ReviewItem>()

    fun setListReview(listReview: ArrayList<ReviewItem>) {
        this.listReview.clear()
        this.listReview = listReview
        notifyDataSetChanged()
    }

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

    override fun onBindViewHolder(holder: ReviewsPagingViewHolder, position: Int) {
        holder.bind(listReview[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewsPagingViewHolder =
        ItemListReviewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            .run {
                ReviewsPagingViewHolder(this)
            }

    override fun getItemCount(): Int = listReview.size
}