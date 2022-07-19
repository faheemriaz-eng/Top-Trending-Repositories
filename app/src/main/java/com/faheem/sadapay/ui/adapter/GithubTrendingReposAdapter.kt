package com.faheem.sadapay.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.faheem.sadapay.data.dtos.Item
import com.faheem.sadapay.databinding.LayoutItemGithubTrendingRepoBinding
import javax.inject.Inject

class GithubTrendingReposAdapter @Inject constructor() :
    RecyclerView.Adapter<GithubTrendingReposViewHolder>() {

    private var list: MutableList<Item> = mutableListOf()
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): GithubTrendingReposViewHolder {
        return GithubTrendingReposViewHolder(
            LayoutItemGithubTrendingRepoBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: GithubTrendingReposViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun setList(list: List<Item>) {
        this.list.clear()
        this.list.addAll(list)
        notifyDataSetChanged()
    }
}