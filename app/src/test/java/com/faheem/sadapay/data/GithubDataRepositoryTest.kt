package com.faheem.sadapay.data

import com.faheem.sadapay.data.dtos.Item
import com.faheem.sadapay.data.dtos.TrendingRepositories
import com.faheem.sadapay.data.local.LocalDataSource
import com.faheem.sadapay.data.remote.RemoteDataSource
import com.faheem.sadapay.data.remote.base.NetworkResult
import com.faheem.sadapay.utils.CoroutineRule
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Rule
import org.junit.Test


@ExperimentalCoroutinesApi
class GithubDataRepositoryTest {
    @Rule
    @JvmField
    var mainCoroutineRule = CoroutineRule()

    lateinit var sut: GithubDataRepositorySource

    @Test
    fun `test fetch repositories from cache on second call`() = runTest {
        val mockLocalSource = mockk<LocalDataSource>()
        val mockResponse = mockk<TrendingRepositories> {
            coEvery { items } returns listOf(Item())
        }
        every { mockLocalSource.saveTrendingRepositories(mockResponse) } returns true

        val mockRemoteSource = mockk<RemoteDataSource> {
            coEvery { fetchRepositories() } returns NetworkResult.Success(mockResponse)
        }

        every { mockLocalSource.getCachedTrendingRepos() } returns null

        sut = GithubDataRepository(mockRemoteSource, mockLocalSource)

        val actualResultOnFirstCall =
            sut.loadTrendingRepositories(isUsingCache = true) as NetworkResult.Success

        every { mockLocalSource.getCachedTrendingRepos() } returns actualResultOnFirstCall.data

        val actualResultOnSecondCall =
            sut.loadTrendingRepositories(isUsingCache = true) as NetworkResult.Success


        Assert.assertEquals(
            listOf(Item()),
            actualResultOnFirstCall.data.items
        )

        Assert.assertEquals(
            listOf(Item()),
            actualResultOnSecondCall.data.items
        )

        coVerify {
            mockLocalSource.getCachedTrendingRepos()
            mockLocalSource.saveTrendingRepositories(mockResponse)
            mockRemoteSource.fetchRepositories()
        }
    }

}