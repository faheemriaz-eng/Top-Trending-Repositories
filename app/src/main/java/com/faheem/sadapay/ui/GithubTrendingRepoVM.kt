package com.faheem.sadapay.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.faheem.sadapay.data.dtos.Item
import com.faheem.sadapay.data.remote.GithubDataSource
import com.faheem.sadapay.data.remote.base.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GithubTrendingRepoVM @Inject constructor(private val trendingRepositoriesProvider: GithubDataSource) :
    ViewModel(), IGithubTrendingRepo {
    private val _trendingRepos: MutableLiveData<List<Item>> = MutableLiveData()
    override val trendingRepos: LiveData<List<Item>> = _trendingRepos

    override fun loadTrendingRepositories(refresh: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            when (val response = trendingRepositoriesProvider.fetchRepositories(!refresh)) {
                is NetworkResult.Success -> {
                    _trendingRepos.postValue(response.data.items)
                }
                is NetworkResult.Error -> {
                    _trendingRepos.value = listOf()
                    _trendingRepos.postValue(listOf())
                }
            }
        }
    }
}