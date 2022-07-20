package com.faheem.sadapay.data

import com.faheem.sadapay.data.dtos.TrendingRepositories
import com.faheem.sadapay.data.remote.base.NetworkResult

interface GithubDataRepositorySource {
    suspend fun loadTrendingRepositories(isUsingCache: Boolean = true): NetworkResult<TrendingRepositories>
}