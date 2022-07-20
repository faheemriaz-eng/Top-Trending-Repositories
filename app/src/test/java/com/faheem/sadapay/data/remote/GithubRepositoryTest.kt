package com.faheem.sadapay.data.remote

import com.faheem.sadapay.data.dtos.TrendingRepositories
import com.faheem.sadapay.data.remote.base.NetworkResult
import com.faheem.sadapay.utils.CoroutineRule
import com.faheem.sadapay.utils.ReadAssetFile
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import io.mockk.coEvery
import io.mockk.coVerify
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

    @Rule
    @JvmField
    var mainCoroutineRule = CoroutineRule()

    lateinit var sut: RemoteDataSource

    @Test
    fun `test fetch repositories from service`() = runTest {
        val mockResponse = Response.success(readJsonFile("MockTrendingRepositories.json"))
        val githubMockService = mockk<GithubService>()
        coEvery { githubMockService.loadTrendingRepositories() } returns mockResponse

        sut = RemoteRepository(githubMockService)

        val actualRepositories = sut.fetchRepositories() as NetworkResult.Success
        Assert.assertEquals(actualRepositories.data.items, mockResponse.body()?.items)

        coVerify {
            githubMockService.loadTrendingRepositories()
        }
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

        sut = RemoteRepository(githubMockService)

        val actualRepositories = sut.fetchRepositories() as NetworkResult.Error
        Assert.assertEquals(
            actualRepositories.error.message,
            mockErrorResponse.errorBody()?.string()
        )

        coVerify {
            githubMockService.loadTrendingRepositories()
        }
    }

    private fun readJsonFile(fileName: String): TrendingRepositories {
        val gson = GsonBuilder().create()
        val itemType = object : TypeToken<TrendingRepositories>() {}.type
        return gson.fromJson(ReadAssetFile.readFileFromTestResources(fileName), itemType)
    }
}