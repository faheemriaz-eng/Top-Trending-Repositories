package com.faheem.sadapay.data

import com.faheem.sadapay.model.NetworkResult
import com.faheem.sadapay.model.TrendingRepositories

class GithubRepository constructor(private val githubService: GithubService) :
    TrendingRepositoriesProvider,BaseRepository() {

    override suspend fun fetchRepositories(isUsingCache: Boolean): NetworkResult<TrendingRepositories> {
       return safeApiCall { githubService.loadTrendingRepositories() }
    }
}