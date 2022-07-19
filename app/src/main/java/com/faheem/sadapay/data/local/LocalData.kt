package com.faheem.sadapay.data.local

import android.content.Context
import android.content.SharedPreferences
import com.faheem.sadapay.data.dtos.TrendingRepositories
import com.faheem.sadapay.utils.SHARED_PREFERENCES_FILE_NAME
import com.faheem.sadapay.utils.TRENDING_REPOSITORIES_KEY
import com.google.gson.Gson
import javax.inject.Inject

class LocalData @Inject constructor(private val context: Context) : LocalDataSource {

    override fun getCachedTrendingRepos(): TrendingRepositories? {
        val sharedPref = context.getSharedPreferences(SHARED_PREFERENCES_FILE_NAME, 0)

        return Gson().fromJson(
            sharedPref.getString(TRENDING_REPOSITORIES_KEY, null),
            TrendingRepositories::class.java
        )
    }

    override fun saveTrendingRepositories(repos: TrendingRepositories): Boolean {
        val sharedPref = context.getSharedPreferences(SHARED_PREFERENCES_FILE_NAME, 0)

        val editor: SharedPreferences.Editor = sharedPref.edit()
        editor.putString(TRENDING_REPOSITORIES_KEY, Gson().toJson(repos))
        editor.apply()
        return editor.commit()
    }
}