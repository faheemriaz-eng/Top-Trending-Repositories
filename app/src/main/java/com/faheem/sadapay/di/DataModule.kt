package com.faheem.sadapay.di

import com.faheem.sadapay.data.remote.GithubDataSource
import com.faheem.sadapay.data.remote.GithubRepository
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
    abstract fun provideGithubRepository(githubRepository: GithubRepository): GithubDataSource
}