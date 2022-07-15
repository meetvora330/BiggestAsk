package com.biggestAsk.data.source.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.NetworkInfo
import android.os.Build
import android.util.Log
import com.biggestAsk.util.Constants
import com.biggestAsk.util.Constants.ERROR_BODY_MESSAGE
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Response


sealed class NetworkResult<T>(
    val data: T? = null,
    val message: String? = null
) {
    class Success<T>(data: T?) : NetworkResult<T>(data)
    class Error<T>(
        message: String,
        data: T? = null
    ) : NetworkResult<T>(
        data,
        message
    )

    class Loading<T> : NetworkResult<T>()
}

abstract class BaseApiResponse(val context: Context) {
    suspend fun <T> safeApiCall(apiCall: suspend () -> Response<T>): NetworkResult<T> {
        try {
            return if (isInternetAvailable(context)) {
                val response = apiCall()
                when {
                    response.isSuccessful -> {
                        val body = response.body()
                        NetworkResult.Success(body)
                    }
                    response.code() == Constants.SERVER_ERROR -> {
                        error(response.errorBody().toString())
                    }
                    response.errorBody() != null -> {
                        Log.d("TAG", "safeApiCall: ${response.errorBody().toString()}")
                        error(apiErrorBody(response.errorBody()!!))
                    }
                    response.code() == Constants.UNAUTHORISED -> {
                        error(response.errorBody().toString())
                    }
                    else -> error("${response.code()} ${response.message()}")
                }
            } else {
                error("No Internet available")
            }

        } catch (e: Exception) {
            return error(
                e.message
                    ?: e.toString()
            )
        }
    }

    private fun apiErrorBody(errorBody: ResponseBody): String {
        val jsonObj = JSONObject(errorBody.string())
        var msg = ""
        if (jsonObj.has(ERROR_BODY_MESSAGE)) {
            msg = jsonObj.getString(ERROR_BODY_MESSAGE)
        }
        return msg
    }

    private fun <T> error(errorMessage: String): NetworkResult<T> =
        NetworkResult.Error(errorMessage)
}

fun isInternetAvailable(context: Context): Boolean {
    var result = false
    val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        connectivityManager?.let {
            it.getNetworkCapabilities(connectivityManager.activeNetwork)
                ?.apply {
                    result = when {
                        hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                        hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                        else -> false
                    }
                }
        }
    } else {
        val activeNetwork: NetworkInfo? = connectivityManager?.activeNetworkInfo
        result = activeNetwork?.isConnectedOrConnecting == true
    }
    return result
}

