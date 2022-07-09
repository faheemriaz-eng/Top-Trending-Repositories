package com.faheem.sadapay.data

import com.faheem.sadapay.CoroutineRule
import com.faheem.sadapay.ReadAssetFile
import com.faheem.sadapay.model.TrendingRepositories
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
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
        val sut = GithubRepository(githubMockService)

        mainCoroutineRule.runBlockingTest {
            val actualRepositories = sut.fetchRepositories()
            Assert.assertEquals(actualRepositories.items, mockResponse.body()?.items)
        }
    }

    private fun readJsonFile(fileName: String): TrendingRepositories {
        val gson = GsonBuilder().create()
        val itemType = object : TypeToken<TrendingRepositories>() {}.type
        return gson.fromJson(ReadAssetFile.readFileFromTestResources(fileName), itemType)
    }
}