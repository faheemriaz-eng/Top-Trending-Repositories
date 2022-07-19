package com.faheem.sadapay.data.dtos

import com.google.gson.annotations.SerializedName


data class TrendingRepositories(
    @SerializedName("incomplete_results")
    val incompleteResults: Boolean? = null,
    @SerializedName("items")
    val items: List<Item>? = null,
    @SerializedName("total_count")
    val totalCount: Int? = null
)

data class Item(
    @SerializedName("name")
    var name: String? = null,
    @SerializedName("full_name")
    val fullName: String? = null,
    @SerializedName("description")
    val description: String? = null,
    @SerializedName("language")
    val language: String? = null,
    @SerializedName("stargazers_count")
    val stargazersCount: Int? = null,
    @SerializedName("owner")
    val owner: Owner? = null
)

data class Owner(
    @SerializedName("login")
    val login: String? = null,
    @SerializedName("avatar_url")
    val avatarUrl: String? = null
)