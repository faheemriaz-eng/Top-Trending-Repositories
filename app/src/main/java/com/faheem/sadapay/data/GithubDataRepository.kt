package com.faheem.sadapay.data

import com.faheem.sadapay.data.dtos.TrendingRepositories
import com.faheem.sadapay.data.local.LocalDataSource
import com.faheem.sadapay.data.remote.RemoteDataSource
import com.faheem.sadapay.data.remote.base.NetworkResult
import javax.inject.Inject

class GithubDataRepository @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource
) : GithubDataRepositorySource {

    override suspend fun loadTrendingRepositories(isUsingCache: Boolean): NetworkResult<TrendingRepositories> {
        return if (isUsingCache && null != localDataSource.getCachedTrendingRepos()) {
            NetworkResult.Success(localDataSource.getCachedTrendingRepos()!!)
        } else {
            val response = remoteDataSource.fetchRepositories()
            if (response is NetworkResult.Success)
                localDataSource.saveTrendingRepositories(response.data)

            return response
        }
    }

}