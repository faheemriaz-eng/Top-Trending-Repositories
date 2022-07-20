package com.faheem.sadapay.di

import com.faheem.sadapay.data.GithubDataRepository
import com.faheem.sadapay.data.GithubDataRepositorySource
import com.faheem.sadapay.data.local.LocalData
import com.faheem.sadapay.data.local.LocalDataSource
import com.faheem.sadapay.data.remote.RemoteDataSource
import com.faheem.sadapay.data.remote.RemoteRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {
    @Binds
    @Singleton
    abstract fun provideRemoteRepository(githubRepository: RemoteRepository): RemoteDataSource

    @Binds
    @Singleton
    abstract fun providesLocalData(localData: LocalData): LocalDataSource

    @Binds
    @Singleton
    abstract fun provideGithubDataRepository(githubDataRepository: GithubDataRepository): GithubDataRepositorySource
}