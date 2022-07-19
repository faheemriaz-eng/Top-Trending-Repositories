package com.faheem.sadapay.data.remote

import com.faheem.sadapay.data.dtos.TrendingRepositories
import com.faheem.sadapay.data.remote.base.NetworkResult

interface GithubDataSource {
    suspend fun fetchRepositories(isUsingCache: Boolean = true): NetworkResult<TrendingRepositories>
}