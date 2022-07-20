package com.faheem.sadapay.ui.adapter

import android.view.View
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.faheem.sadapay.data.dtos.Item
import com.faheem.sadapay.databinding.LayoutItemGithubTrendingRepoBinding

class GithubTrendingReposViewHolder(private val itemBinding: LayoutItemGithubTrendingRepoBinding) :
    RecyclerView.ViewHolder(itemBinding.root) {
    init {
        itemView.setOnClickListener {
            if (itemBinding.expandableLayout.isVisible)
                itemBinding.expandableLayout.visibility = View.GONE
            else
                itemBinding.expandableLayout.visibility = View.VISIBLE
        }
    }

    fun bind(item: Item) {
        with(itemBinding) {
            tvOwnerName.text = item.name
            tvRepoName.text = item.fullName
            tvDetails.text = item.description
            tvTopicName.text = item.language ?: item.name
            tvStarCount.text = item.stargazersCount?.toString()

            Glide.with(ivProfile.context)
                .load(item.owner?.avatarUrl)
                .diskCacheStrategy(DiskCacheStrategy.DATA)
                .into(ivProfile)
        }
    }
}
