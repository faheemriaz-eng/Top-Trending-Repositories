package com.faheem.sadapay.di

import android.content.Context
import com.faheem.sadapay.data.GithubService
import com.faheem.sadapay.data.LocalData
import com.faheem.sadapay.data.LocalDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    @Provides
    fun providesGithubReposService(): GithubService = GithubService.create()

    @Provides
    fun providesLocalData(@ApplicationContext context: Context): LocalDataSource =
        LocalData(context)

}