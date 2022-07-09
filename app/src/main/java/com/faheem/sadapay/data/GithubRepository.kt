package com.faheem.sadapay.data

import com.faheem.sadapay.model.TrendingRepositories

class GithubRepository constructor(private val githubService: GithubService) :
    TrendingRepositoriesProvider {

    override suspend fun fetchRepositories(): TrendingRepositories {
        val response = githubService.loadTrendingRepositories()
        if (response.isSuccessful)
            return response.body()?.let { return@let it } ?: TrendingRepositories()
        else
            return TrendingRepositories()
    }

}