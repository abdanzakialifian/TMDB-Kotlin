package com.application.zaki.movies.presentation.detail.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.application.zaki.movies.databinding.ItemListCastCrewBinding
import com.application.zaki.movies.domain.model.CastCrewItem
import com.application.zaki.movies.utils.loadImageUrl
import javax.inject.Inject

class CastCrewAdapter @Inject constructor() :
    ListAdapter<CastCrewItem, CastCrewAdapter.CastViewHolder>(DIFF_CALLBACK) {
    inner class CastViewHolder(private val binding: ItemListCastCrewBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: CastCrewItem) {
            binding.apply {
                imgPeople.loadImageUrl(item.profilePath ?: "")
                tvTitle.text = item.name
                tvSubTitle.text = item.character ?: item.job
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
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<CastCrewItem>() {
            override fun areItemsTheSame(oldItem: CastCrewItem, newItem: CastCrewItem): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: CastCrewItem, newItem: CastCrewItem): Boolean =
                oldItem == newItem
        }
    }
}