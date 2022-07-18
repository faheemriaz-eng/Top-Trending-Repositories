package com.faheem.sadapay.data

import com.faheem.sadapay.model.TrendingRepositories

interface LocalDataSource {
    fun getCachedTrendingRepos(): TrendingRepositories?
    fun saveTrendingRepositories(repos: TrendingRepositories): Boolean
}