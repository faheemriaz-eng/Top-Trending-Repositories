package com.faheem.sadapay.ui

import androidx.lifecycle.LiveData
import com.faheem.sadapay.model.Item

interface IGithubTrendingRepo {
    val trendingRepos: LiveData<List<Item>>
    fun loadTrendingRepositories()
}