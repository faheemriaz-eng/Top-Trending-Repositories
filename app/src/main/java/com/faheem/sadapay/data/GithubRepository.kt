package com.faheem.sadapay.data

import com.faheem.sadapay.model.NetworkResult
import com.faheem.sadapay.model.TrendingRepositories

class GithubRepository constructor(
    private val githubService: GithubService,
    private val localData: LocalDataSource
) : TrendingRepositoriesProvider, BaseRepository() {

    override suspend fun fetchRepositories(isUsingCache: Boolean): NetworkResult<TrendingRepositories> {
        return if (isUsingCache) {
            localData.getCachedTrendingRepos()?.let {
                NetworkResult.Success(it)
            } ?: safeApiCall { githubService.loadTrendingRepositories() }
        } else
            safeApiCall { githubService.loadTrendingRepositories() }
    }
}