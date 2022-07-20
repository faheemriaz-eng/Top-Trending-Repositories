package com.faheem.sadapay.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.faheem.sadapay.data.GithubDataRepositorySource
import com.faheem.sadapay.data.dtos.Item
import com.faheem.sadapay.data.remote.base.NetworkResult
import com.faheem.sadapay.utils.CoroutineRule
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class GithubTrendingRepoVMTest {
    @Rule
    @JvmField
    var mainCoroutineRule = CoroutineRule()

    @Rule
    @JvmField
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    lateinit var sut: GithubTrendingRepoVM

    @Test
    fun `test load trending repositories with success`() = runTest {
        val mockGithubRepository = mockk<GithubDataRepositorySource> {
            coEvery { loadTrendingRepositories() } returns NetworkResult.Success(
                mockk { coEvery { items } returns listOf(Item()) })
        }
        sut = GithubTrendingRepoVM(mockGithubRepository)
        sut.loadTrendingRepositories()

        Assert.assertEquals(listOf(Item()), sut.trendingRepos.getOrAwaitValue())

        coVerify {
            mockGithubRepository.loadTrendingRepositories()
        }
    }

    @Test
    fun `test load trending repositories with failure`() = runTest {
        val mockGithubRepository = mockk<GithubDataRepositorySource> {
            coEvery { loadTrendingRepositories() } returns NetworkResult.Error(
                mockk { coEvery { message } returns "This request unfortunately failed please try again" })
        }
        sut = GithubTrendingRepoVM(mockGithubRepository)
        sut.loadTrendingRepositories()

        Assert.assertEquals(listOf<Item>(), sut.trendingRepos.getOrAwaitValue())

        coVerify {
            mockGithubRepository.loadTrendingRepositories()
        }
    }

    @Test
    fun `test load trending repositories with force refresh`() = runTest {
        val mockGithubRepository = mockk<GithubDataRepositorySource> {
            coEvery { loadTrendingRepositories(isUsingCache = false) } returns NetworkResult.Success(
                mockk { coEvery { items } returns listOf(Item()) })
        }
        sut = GithubTrendingRepoVM(mockGithubRepository)
        sut.loadTrendingRepositories(refresh = true)

        Assert.assertEquals(listOf(Item()), sut.trendingRepos.getOrAwaitValue())

        coVerify {
            mockGithubRepository.loadTrendingRepositories(isUsingCache = false)
        }
    }
}