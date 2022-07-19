package com.faheem.sadapay.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.faheem.sadapay.data.TrendingRepositoriesProvider
import com.faheem.sadapay.model.Item
import com.faheem.sadapay.model.NetworkResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class GithubTrendingRepoVM(private val trendingRepositoriesProvider: TrendingRepositoriesProvider) :
    ViewModel(), IGithubTrendingRepo {
    private val _trendingRepos: MutableLiveData<List<Item>> = MutableLiveData()
    override val trendingRepos: LiveData<List<Item>> = _trendingRepos

    override fun loadTrendingRepositories() {
        viewModelScope.launch(Dispatchers.IO) {
            when (val response = trendingRepositoriesProvider.fetchRepositories()) {
                is NetworkResult.Success -> {
                    _trendingRepos.value = response.data.items
                }
                is NetworkResult.Error -> {
                }
            }
        }
    }
}