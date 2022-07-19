package com.faheem.sadapay.ui.adapter

import android.view.View
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
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
            tvTopicName.text = item.language?.let {
                it
            } ?: item.name

            tvStarCount.text = item.stargazersCount?.toString()
        }
    }
}
