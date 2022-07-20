package com.faheem.sadapay.di

import com.faheem.sadapay.data.remote.GithubService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    @Provides
    fun providesGithubReposService(): GithubService = GithubService.create()

}