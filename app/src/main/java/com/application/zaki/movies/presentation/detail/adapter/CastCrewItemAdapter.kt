package com.application.zaki.movies.presentation.detail.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.application.zaki.movies.databinding.ItemListCastCrewItemBinding
import com.application.zaki.movies.domain.model.CastCrewItemModel
import com.application.zaki.movies.utils.loadImageUrl
import javax.inject.Inject

class CastCrewItemAdapter @Inject constructor() :
    ListAdapter<CastCrewItemModel, CastCrewItemAdapter.CastViewHolder>(DIFF_CALLBACK) {
    inner class CastViewHolder(private val binding: ItemListCastCrewItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: CastCrewItemModel) {
            binding.apply {
                imgPeople.loadImageUrl(item.profilePath ?: "")
                tvTitle.text = item.name
                tvSubTitle.text = item.character ?: item.job
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CastViewHolder =
        ItemListCastCrewItemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        ).run {
            CastViewHolder(this)
        }

    override fun onBindViewHolder(holder: CastViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<CastCrewItemModel>() {
            override fun areItemsTheSame(
                oldItem: CastCrewItemModel,
                newItem: CastCrewItemModel
            ): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(
                oldItem: CastCrewItemModel,
                newItem: CastCrewItemModel
            ): Boolean =
                oldItem == newItem
        }
    }
}