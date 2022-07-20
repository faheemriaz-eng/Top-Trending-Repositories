package com.faheem.sadapay.data.remote

import com.faheem.sadapay.data.dtos.TrendingRepositories
import com.faheem.sadapay.data.remote.base.NetworkResult

interface RemoteDataSource {
    suspend fun fetchRepositories(): NetworkResult<TrendingRepositories>
}