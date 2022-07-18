package com.faheem.sadapay.data

import android.content.Context
import android.content.SharedPreferences
import com.faheem.sadapay.CoroutineRule
import com.faheem.sadapay.ReadAssetFile
import com.faheem.sadapay.model.NetworkResult
import com.faheem.sadapay.model.TrendingRepositories
import com.google.gson.Gson
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
        coEvery { githubMockService.loadTrendingRepositories() } returns mockResponse
        val sut = GithubRepository(githubMockService, mockk())

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
    fun `test fetch repositories from cache`() {
        val mockContext = mockk<Context>()
        val githubMockService = mockk<GithubService>()

        val sut = GithubRepository(githubMockService, LocalData(mockContext))

        val sharedPref = mockk<SharedPreferences>()
        val mockTrendingRepo = Gson().toJson(readJsonFile("MockTrendingRepositories.json"))
        every {mockContext.getSharedPreferences(SHARED_PREFERENCES_FILE_NAME,0) } returns sharedPref
        every { sharedPref.getString(TRENDING_REPOSITORIES_KEY, null) } returns mockTrendingRepo

        mainCoroutineRule.runBlockingTest {
            val expectedResult = NetworkResult.Success(readJsonFile("MockTrendingRepositories.json"))
            val actualResult = sut.fetchRepositories(isUsingCache = true) as NetworkResult.Success
            Assert.assertEquals(expectedResult.data.items, actualResult.data.items)
        }
    }

    private fun readJsonFile(fileName: String): TrendingRepositories {
        val gson = GsonBuilder().create()
        val itemType = object : TypeToken<TrendingRepositories>() {}.type
        return gson.fromJson(ReadAssetFile.readFileFromTestResources(fileName), itemType)
    }
}