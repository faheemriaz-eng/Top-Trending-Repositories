package com.faheem.sadapay.data.local

import com.faheem.sadapay.data.dtos.TrendingRepositories

interface LocalDataSource {
    fun getCachedTrendingRepos(): TrendingRepositories?
    fun saveTrendingRepositories(repos: TrendingRepositories): Boolean
}