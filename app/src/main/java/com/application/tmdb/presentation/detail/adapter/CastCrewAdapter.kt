package com.application.tmdb.presentation.detail.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.application.tmdb.databinding.ItemListCastCrewBinding
import com.application.tmdb.core.domain.model.CastCrewItemModel
import com.application.tmdb.core.domain.model.CastCrewModel
import com.application.tmdb.common.Category
import com.application.tmdb.common.gone
import com.application.tmdb.common.visible

class CastCrewAdapter :
    ListAdapter<CastCrewModel, CastCrewAdapter.CastViewHolder>(DIFF_CALLBACK) {

    private lateinit var castCrewItemAdapter: CastCrewItemAdapter

    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

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
                    castCrewItemAdapter = CastCrewItemAdapter()
                    if (this@CastCrewAdapter::castCrewItemAdapter.isInitialized) {
                        castCrewItemAdapter.submitList(item.castCrews)
                        rvCastCrew.adapter = castCrewItemAdapter
                        rvCastCrew.setHasFixedSize(true)
                        eventListeners(item.category)
                    }
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

    private fun eventListeners(category: com.application.tmdb.common.Category) {
        castCrewItemAdapter.setOnItemClickCallback(object :
            CastCrewItemAdapter.OnItemClickCallback {
            override fun onItemClicked(item: CastCrewItemModel) {
                if (this@CastCrewAdapter::castCrewItemAdapter.isInitialized) {
                    onItemClickCallback.onItemClicked(item.id ?: 0, category)
                }
            }
        })
    }

    interface OnItemClickCallback {
        fun onItemClicked(id: Int, category: com.application.tmdb.common.Category)
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