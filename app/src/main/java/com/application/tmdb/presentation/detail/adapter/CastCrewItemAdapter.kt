package com.application.tmdb.presentation.detail.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.application.tmdb.databinding.ItemListCastCrewItemBinding
import com.application.tmdb.domain.model.CastCrewItemModel
import com.application.tmdb.utils.loadImageUrl

class CastCrewItemAdapter :
    ListAdapter<CastCrewItemModel, CastCrewItemAdapter.CastViewHolder>(DIFF_CALLBACK) {

    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    inner class CastViewHolder(private val binding: ItemListCastCrewItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: CastCrewItemModel) {
            binding.apply {
                imgPeople.loadImageUrl(item.profilePath ?: "")
                tvTitle.text = item.name
                tvSubTitle.text = item.character ?: item.job
            }

            if (this@CastCrewItemAdapter::onItemClickCallback.isInitialized) {
                itemView.setOnClickListener {
                    onItemClickCallback.onItemClicked(item)
                }
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

    interface OnItemClickCallback {
        fun onItemClicked(item: CastCrewItemModel)
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