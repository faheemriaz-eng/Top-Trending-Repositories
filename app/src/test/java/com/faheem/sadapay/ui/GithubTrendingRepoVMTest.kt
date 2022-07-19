package com.faheem.sadapay.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.faheem.sadapay.CoroutineRule
import com.faheem.sadapay.data.TrendingRepositoriesProvider
import com.faheem.sadapay.model.Item
import com.faheem.sadapay.model.NetworkResult
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

    @Test
    fun `test load trending repositories with success`() = runTest {
        val mockGithubRepository = mockk<TrendingRepositoriesProvider> {
            coEvery { fetchRepositories() } returns NetworkResult.Success(
                mockk { coEvery { items } returns listOf(Item()) })
        }
        val sut = GithubTrendingRepoVM(mockGithubRepository)
        sut.loadTrendingRepositories()

        Assert.assertEquals(listOf(Item()), sut.trendingRepos.getOrAwaitValue())

        coVerify {
            mockGithubRepository.fetchRepositories()
        }
    }

    @Test
    fun `test load trending repositories with failure`() = runTest {
        val mockGithubRepository = mockk<TrendingRepositoriesProvider> {
            coEvery { fetchRepositories() } returns NetworkResult.Error(
                mockk { coEvery { message } returns "This request unfortunately failed please try again" })
        }
        val sut = GithubTrendingRepoVM(mockGithubRepository)
        sut.loadTrendingRepositories()

        Assert.assertEquals(listOf<Item>(), sut.trendingRepos.getOrAwaitValue())

        coVerify {
            mockGithubRepository.fetchRepositories()
        }
    }
}