package com.application.zaki.movies.presentation.detail.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.application.zaki.movies.databinding.ItemListCastCrewBinding
import com.application.zaki.movies.domain.model.CastCrewModel
import com.application.zaki.movies.utils.gone
import com.application.zaki.movies.utils.visible
import javax.inject.Inject

class CastCrewAdapter @Inject constructor() :
    ListAdapter<CastCrewModel, CastCrewAdapter.CastViewHolder>(DIFF_CALLBACK) {

    inner class CastViewHolder(private val binding: ItemListCastCrewBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: CastCrewModel) {
            binding.apply {
                if (item.castCrews.isNullOrEmpty()) {
                    tvCastCrew.gone()
                    rvCastCrew.gone()
                } else {
                    tvCastCrew.visible()
                    rvCastCrew.visible()
                    tvCastCrew.text = item.title
                    val castCrewItemAdapter = CastCrewItemAdapter()
                    castCrewItemAdapter.submitList(item.castCrews)
                    rvCastCrew.adapter = castCrewItemAdapter
                    rvCastCrew.setHasFixedSize(true)
                }
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
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<CastCrewModel>() {
            override fun areItemsTheSame(oldItem: CastCrewModel, newItem: CastCrewModel): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(
                oldItem: CastCrewModel,
                newItem: CastCrewModel
            ): Boolean =
                oldItem == newItem
        }
    }
}