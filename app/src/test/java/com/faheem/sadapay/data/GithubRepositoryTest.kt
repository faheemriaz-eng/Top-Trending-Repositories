package com.faheem.sadapay.data

import com.faheem.sadapay.CoroutineRule
import com.faheem.sadapay.ReadAssetFile
import com.faheem.sadapay.model.NetworkResult
import com.faheem.sadapay.model.TrendingRepositories
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import retrofit2.Response


class GithubRepositoryTest {

    // Set the main coroutines dispatcher for unit testing.
    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = CoroutineRule()

    @Test
    fun `test fetch repositories from service`() {
        val mockResponse = Response.success(readJsonFile("MockTrendingRepositories.json"))
        val githubMockService = mockk<GithubService>()
        val mockLocalData = mockk<LocalData>()
        every { mockLocalData.saveTrendingRepositories(mockResponse.body()!!) } returns true
        coEvery { githubMockService.loadTrendingRepositories() } returns mockResponse

        val sut = GithubRepository(githubMockService, mockLocalData)

        mainCoroutineRule.runBlockingTest {
            val actualRepositories = sut.fetchRepositories(false) as NetworkResult.Success
            Assert.assertEquals(actualRepositories.data.items, mockResponse.body()?.items)
        }
    }

    @Test
    fun `test fetch repositories from service is failed with error code 400`() {
        val errorMessage = "This request unfortunately failed please try again"
        val mockErrorResponse = Response.error<TrendingRepositories>(
            400,
            errorMessage.toResponseBody("application/json".toMediaTypeOrNull())
        )
        val githubMockService = mockk<GithubService>()
        coEvery { githubMockService.loadTrendingRepositories() } returns mockErrorResponse
        val sut = GithubRepository(githubMockService, mockk())

        mainCoroutineRule.runBlockingTest {
            val actualRepositories = sut.fetchRepositories(false) as NetworkResult.Error
            Assert.assertEquals(
                actualRepositories.error.message,
                mockErrorResponse.errorBody()?.string()
            )
        }
    }

    @Test
    fun `test fetch repositories from cache on second call`() {
        val mockResponse = Response.success(readJsonFile("MockTrendingRepositories.json"))
        val githubMockService = mockk<GithubService>()
        val mockLocalData = mockk<LocalData>()

        every { mockLocalData.getCachedTrendingRepos() } returns null
        every { mockLocalData.saveTrendingRepositories(mockResponse.body()!!) } returns true
        coEvery { githubMockService.loadTrendingRepositories() } returns mockResponse

        val sut = GithubRepository(githubMockService, mockLocalData)


        mainCoroutineRule.runBlockingTest {
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
    }

    private fun readJsonFile(fileName: String): TrendingRepositories {
        val gson = GsonBuilder().create()
        val itemType = object : TypeToken<TrendingRepositories>() {}.type
        return gson.fromJson(ReadAssetFile.readFileFromTestResources(fileName), itemType)
    }
}