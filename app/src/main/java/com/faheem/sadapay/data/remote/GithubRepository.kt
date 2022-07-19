package com.faheem.sadapay.data.remote

import com.faheem.sadapay.data.dtos.TrendingRepositories
import com.faheem.sadapay.data.local.LocalDataSource
import com.faheem.sadapay.data.remote.base.BaseRepository
import com.faheem.sadapay.data.remote.base.NetworkResult
import javax.inject.Inject

class GithubRepository @Inject constructor(
    private val githubService: GithubService,
    private val localData: LocalDataSource
) : GithubDataSource, BaseRepository() {

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