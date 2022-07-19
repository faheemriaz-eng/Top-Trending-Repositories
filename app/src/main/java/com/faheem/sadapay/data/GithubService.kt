package com.faheem.sadapay.data

import androidx.viewbinding.BuildConfig
import com.faheem.sadapay.model.TrendingRepositories
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

interface GithubService {

    @GET("search/repositories?q=language=+sort:stars")
    suspend fun loadTrendingRepositories(): Response<TrendingRepositories>

    companion object {
        private const val BASE_URL = "https://api.github.com/"

        fun create(): GithubService {
            val loggingInterceptor = HttpLoggingInterceptor()
            if (BuildConfig.DEBUG) {
                loggingInterceptor.apply { level = HttpLoggingInterceptor.Level.BODY }
            }
            val client = OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .build()

            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(GithubService::class.java)
        }
    }
}