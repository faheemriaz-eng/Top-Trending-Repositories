package com.faheem.sadapay.data

import com.faheem.sadapay.model.NetworkResult
import com.faheem.sadapay.model.TrendingRepositories

class GithubRepository constructor(
    private val githubService: GithubService,
    private val localData: LocalDataSource
) : TrendingRepositoriesProvider, BaseRepository() {

    override suspend fun fetchRepositories(isUsingCache: Boolean): NetworkResult<TrendingRepositories> {
        return if (isUsingCache && null != localData.getCachedTrendingRepos()) {
            NetworkResult.Success(localData.getCachedTrendingRepos()!!)
        } else {
            val response = safeApiCall { githubService.loadTrendingRepositories() }
            if (response is NetworkResult.Success)
                localData.saveTrendingRepositories(response.data)

            return response
        }
    }
}