package com.faheem.sadapay.data

import com.faheem.sadapay.model.NetworkResult
import com.faheem.sadapay.model.TrendingRepositories

interface TrendingRepositoriesProvider {
    suspend fun fetchRepositories(isUsingCache: Boolean = true): NetworkResult<TrendingRepositories>
}