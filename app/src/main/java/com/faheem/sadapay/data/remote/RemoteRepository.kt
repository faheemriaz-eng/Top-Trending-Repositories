package com.faheem.sadapay.data.remote

import com.faheem.sadapay.data.dtos.TrendingRepositories
import com.faheem.sadapay.data.remote.base.BaseRepository
import com.faheem.sadapay.data.remote.base.NetworkResult
import javax.inject.Inject

class RemoteRepository @Inject constructor(
    private val githubService: GithubService
) : RemoteDataSource, BaseRepository() {

    override suspend fun fetchRepositories(): NetworkResult<TrendingRepositories> {
        return safeApiCall { githubService.loadTrendingRepositories() }
    }
}