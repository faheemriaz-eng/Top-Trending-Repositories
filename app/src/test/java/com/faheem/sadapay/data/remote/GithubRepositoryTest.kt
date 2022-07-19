package com.faheem.sadapay.data.remote

import com.faheem.sadapay.data.dtos.TrendingRepositories
import com.faheem.sadapay.data.local.LocalData
import com.faheem.sadapay.data.remote.base.NetworkResult
import com.faheem.sadapay.utils.CoroutineRule
import com.faheem.sadapay.utils.ReadAssetFile
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import retrofit2.Response


@ExperimentalCoroutinesApi
class GithubRepositoryTest {

    // Set the main coroutines dispatcher for unit testing.
    @Rule
    @JvmField
    var mainCoroutineRule = CoroutineRule()

    @Test
    fun `test fetch repositories from service`() = runTest {
        val mockResponse = Response.success(readJsonFile("MockTrendingRepositories.json"))
        val githubMockService = mockk<GithubService>()
        val mockLocalData = mockk<LocalData>()
        every { mockLocalData.saveTrendingRepositories(mockResponse.body()!!) } returns true
        coEvery { githubMockService.loadTrendingRepositories() } returns mockResponse

        val sut = GithubRepository(githubMockService, mockLocalData)

        val actualRepositories = sut.fetchRepositories(false) as NetworkResult.Success
        Assert.assertEquals(actualRepositories.data.items, mockResponse.body()?.items)
    }

    @Test
    fun `test fetch repositories from service is failed with error code 400`() = runTest {
        val errorMessage = "This request unfortunately failed please try again"
        val mockErrorResponse = Response.error<TrendingRepositories>(
            400,
            errorMessage.toResponseBody("application/json".toMediaTypeOrNull())
        )
        val githubMockService = mockk<GithubService>()
        coEvery { githubMockService.loadTrendingRepositories() } returns mockErrorResponse
        val sut = GithubRepository(githubMockService, mockk())

        val actualRepositories = sut.fetchRepositories(false) as NetworkResult.Error
        Assert.assertEquals(
            actualRepositories.error.message,
            mockErrorResponse.errorBody()?.string()
        )

    }

    @Test
    fun `test fetch repositories from cache on second call`() = runTest {
        val mockResponse = Response.success(readJsonFile("MockTrendingRepositories.json"))
        val githubMockService = mockk<GithubService>()
        val mockLocalData = mockk<LocalData>()

        every { mockLocalData.getCachedTrendingRepos() } returns null
        every { mockLocalData.saveTrendingRepositories(mockResponse.body()!!) } returns true
        coEvery { githubMockService.loadTrendingRepositories() } returns mockResponse

        val sut = GithubRepository(githubMockService, mockLocalData)


        val expectedResult =
            NetworkResult.Success(readJsonFile("MockTrendingRepositories.json"))
        val actualResultOnFirstCall =
            sut.fetchRepositories(isUsingCache = true) as NetworkResult.Success

        every { mockLocalData.getCachedTrendingRepos() } returns actualResultOnFirstCall.data

        val actualResultOnSecondCall =
            sut.fetchRepositories(isUsingCache = true) as NetworkResult.Success


        Assert.assertEquals(
            expectedResult.data.items,
            actualResultOnFirstCall.data.items
        )

        Assert.assertEquals(
            expectedResult.data.items,
            actualResultOnSecondCall.data.items
        )
    }

    private fun readJsonFile(fileName: String): TrendingRepositories {
        val gson = GsonBuilder().create()
        val itemType = object : TypeToken<TrendingRepositories>() {}.type
        return gson.fromJson(ReadAssetFile.readFileFromTestResources(fileName), itemType)
    }
}