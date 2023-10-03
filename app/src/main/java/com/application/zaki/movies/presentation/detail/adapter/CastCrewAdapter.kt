package com.application.zaki.movies.presentation.detail.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.application.zaki.movies.databinding.ItemListCastCrewBinding
import com.application.zaki.movies.domain.model.CastItem
import com.application.zaki.movies.utils.loadImageUrl
import javax.inject.Inject

class CastCrewAdapter @Inject constructor() :
    ListAdapter<CastItem, CastCrewAdapter.CastViewHolder>(DIFF_CALLBACK) {
    inner class CastViewHolder(private val binding: ItemListCastCrewBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: CastItem) {
            binding.apply {
                imgPeople.loadImageUrl(item.profilePath ?: "")
                tvTitle.text = item.name
                tvSubTitle.text = item.character
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CastViewHolder =
        ItemListCastCrewBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        ).run {
            CastViewHolder(this)
        }

    override fun onBindViewHolder(holder: CastViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<CastItem>() {
            override fun areItemsTheSame(oldItem: CastItem, newItem: CastItem): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: CastItem, newItem: CastItem): Boolean =
                oldItem == newItem
        }
    }
}