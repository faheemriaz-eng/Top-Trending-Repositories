package com.faheem.sadapay.data

import android.content.Context
import android.content.SharedPreferences
import com.faheem.sadapay.model.NetworkResult
import com.faheem.sadapay.model.TrendingRepositories
import com.google.gson.Gson

class LocalData(private val context: Context) {

    fun getCachedTrendingRepos(): NetworkResult<TrendingRepositories> {
        val sharedPref = context.getSharedPreferences(SHARED_PREFERENCES_FILE_NAME, 0)

        val trendingRepositories = Gson().fromJson(
            sharedPref.getString(TRENDING_REPOSITORIES_KEY, null),
            TrendingRepositories::class.java
        )
        return NetworkResult.Success(trendingRepositories ?: TrendingRepositories())
    }

    fun saveTrendingRepositories(repos: TrendingRepositories): NetworkResult<Boolean> {
        val sharedPref = context.getSharedPreferences(SHARED_PREFERENCES_FILE_NAME, 0)

        val editor: SharedPreferences.Editor = sharedPref.edit()
        editor.putString(TRENDING_REPOSITORIES_KEY, Gson().toJson(repos))
        editor.apply()
        val isSuccess = editor.commit()
        return NetworkResult.Success(isSuccess)
    }
}