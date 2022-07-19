package com.faheem.sadapay.data.dtos

import com.faheem.sadapay.utils.ReadAssetFile
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import org.junit.Assert
import org.junit.Test

class TrendingModelTests {

    @Test
    fun `test json response maps to model`() {
        val trendingRepositories = readJsonFile("MockTrendingRepositories.json")
        Assert.assertEquals(30, trendingRepositories.items?.count())
        Assert.assertEquals("golang/go", trendingRepositories.items?.first()?.fullName)
        Assert.assertEquals("ethereum/solidity", trendingRepositories.items?.last()?.fullName)
    }

    private fun readJsonFile(fileName: String): TrendingRepositories {
        val gson = GsonBuilder().create()
        val itemType = object : TypeToken<TrendingRepositories>() {}.type
        return gson.fromJson(ReadAssetFile.readFileFromTestResources(fileName), itemType)
    }
}