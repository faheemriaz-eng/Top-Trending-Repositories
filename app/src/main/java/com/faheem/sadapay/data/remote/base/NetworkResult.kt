package com.faheem.sadapay.data.remote.base

sealed class NetworkResult<out T: Any>{
    data class Success<out T : Any>(val data:T): NetworkResult<T>()
    data class Error(val error:Exception): NetworkResult<Nothing>()
}
