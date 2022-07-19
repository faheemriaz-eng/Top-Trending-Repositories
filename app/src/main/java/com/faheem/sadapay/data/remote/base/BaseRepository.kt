package com.faheem.sadapay.data.remote.base

import okio.IOException
import retrofit2.Response

open class BaseRepository {
    suspend fun <T : Any> safeApiCall(
        call: suspend () -> Response<T>
    ): NetworkResult<T> {
        return safeApiResult(call)
    }

    private suspend fun <T : Any> safeApiResult(call: suspend () -> Response<T>): NetworkResult<T> {
        try {
            val response = call.invoke()
            if (response.isSuccessful) return NetworkResult.Success(response.body()!!)
            return NetworkResult.Error(IOException(setErrorMessage(response)))
        } catch (exception: IOException) {
            return NetworkResult.Error(exception)
        }
    }

    private fun <T : Any> setErrorMessage(response: Response<T>): String {
        return when (response.code()) {
            403 -> mapError(NetworkErrors.Forbidden)
            404 -> mapError(NetworkErrors.NotFound)
            502 -> mapError(NetworkErrors.BadGateway)
            504 -> mapError(NetworkErrors.NoInternet)
            in 400..500 -> mapError(NetworkErrors.InternalServerError)
            -1009 -> mapError(NetworkErrors.NoInternet)
            -1001 -> mapError(NetworkErrors.RequestTimedOut)
            else -> mapError(NetworkErrors.UnknownError())
        }
    }

    private fun mapError(error: NetworkErrors): String {
        return when (error) {
            is NetworkErrors.NoInternet, NetworkErrors.RequestTimedOut -> "Please check your internet connection"
            is NetworkErrors.BadGateway -> "Bad Gateway"
            is NetworkErrors.NotFound -> "Not Found"
            is NetworkErrors.Forbidden -> "You don't have access to this information"
            is NetworkErrors.InternalServerError, is NetworkErrors.UnknownError -> "This request unfortunately failed please try again"
        }
    }


    sealed class NetworkErrors {
        object NoInternet : NetworkErrors()
        object RequestTimedOut : NetworkErrors()
        object BadGateway : NetworkErrors()
        object NotFound : NetworkErrors()
        object Forbidden : NetworkErrors()
        object InternalServerError : NetworkErrors()
        open class UnknownError : NetworkErrors()
    }
}