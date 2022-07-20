package com.faheem.sadapay.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.faheem.sadapay.data.GithubDataRepositorySource
import com.faheem.sadapay.data.dtos.Item
import com.faheem.sadapay.data.remote.base.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GithubTrendingRepoVM @Inject constructor(private val githubDataSource: GithubDataRepositorySource) :
    ViewModel(), IGithubTrendingRepo {
    private val _trendingRepos: MutableLiveData<List<Item>> = MutableLiveData()
    override val trendingRepos: LiveData<List<Item>> = _trendingRepos

    private val _viewState = MutableLiveData<ViewState>()
    override val viewState: LiveData<ViewState> = _viewState

    init {
        loadTrendingRepositories()
    }

    override fun loadTrendingRepositories(refresh: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            _viewState.postValue(ViewState.Loading)
            when (val response = githubDataSource.loadTrendingRepositories(!refresh)) {
                is NetworkResult.Success -> {
                    _viewState.postValue(ViewState.ReposLoaded(response.data.items))
                    _trendingRepos.postValue(response.data.items)
                }
                is NetworkResult.Error -> {
                    _viewState.postValue(ViewState.ReposLoadFailure(response.error.message ?: ""))
                    _trendingRepos.postValue(listOf())
                }
            }
        }
    }

    sealed class ViewState {
        object Loading : ViewState()
        data class ReposLoaded(val repos: List<Item>?) : ViewState()
        data class ReposLoadFailure(val errorMessage: String) : ViewState()
    }
}