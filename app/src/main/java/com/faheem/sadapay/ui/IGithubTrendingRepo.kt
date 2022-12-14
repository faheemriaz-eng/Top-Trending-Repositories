package com.faheem.sadapay.ui

import androidx.lifecycle.LiveData
import com.faheem.sadapay.data.dtos.Item

interface IGithubTrendingRepo {
    val trendingRepos: LiveData<List<Item>>
    val viewState: LiveData<GithubTrendingRepoVM.ViewState>
    fun loadTrendingRepositories(refresh: Boolean = false)
}