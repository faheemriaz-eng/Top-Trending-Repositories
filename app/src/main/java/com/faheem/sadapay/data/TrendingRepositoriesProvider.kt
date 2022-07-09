package com.faheem.sadapay.data

import com.faheem.sadapay.model.TrendingRepositories
import retrofit2.Response

interface TrendingRepositoriesProvider {
    suspend fun fetchRepositories(): TrendingRepositories
}