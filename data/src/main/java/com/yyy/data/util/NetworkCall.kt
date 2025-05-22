package com.yyy.data.util

import okhttp3.ResponseBody
import okhttp3.internal.http2.StreamResetException
import retrofit2.Response
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.util.concurrent.TimeoutException

suspend fun <T : Any> makeCallWithTryCatch(
    call: suspend () -> Response<T>
): NetworkResponse {
    return try {
        val result = call.invoke()
        if (result.isSuccessful) {
            result.body()?.let { NetworkResponse.Success(it, result.message()) }
                ?: NetworkResponse.Success(Any(), result.message())

        } else {
            NetworkResponse.Error(errorBody = result.errorBody())
        }
    } catch (e: Exception) {
        when (e) {
            is UnknownHostException -> {
                NetworkResponse.Error(Exception("Please check internet connection"))
            }

            is okhttp3.internal.connection.RouteException, is SocketTimeoutException, is TimeoutException, is StreamResetException -> {
                NetworkResponse.Error(Exception("Request Timeout"))
            }

            else -> {
                NetworkResponse.Error(e = e)
            }
        }

    }
}

sealed class NetworkResponse() {
    data class Success(val data: Any, val message: String? = null) : NetworkResponse()
    data class Error(val e: Exception? = null, val errorBody: ResponseBody? = null) :
        NetworkResponse()
}