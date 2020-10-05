package com.sostrovsky.onlineshop.remote.response

import retrofit2.Response
import timber.log.Timber
import java.io.IOException

suspend fun <T : Any> safeApiCall(call: suspend () -> Response<T>, error: String): T? {
    val result = webResponseOutput(
        call,
        error
    )
    var output: T? = null
    when (result) {
        is RemoteResponse.Success -> output = result.output
        is RemoteResponse.Error -> Timber.e("${result.exception}")
    }
    return output

}

private suspend fun <T : Any> webResponseOutput(call: suspend () -> Response<T>, error: String):
        RemoteResponse<T> {
    val response = call.invoke()
    return if (response.isSuccessful)
        RemoteResponse.Success(response.body()!!)
    else
        RemoteResponse.Error(
            IOException(
                error
            )
        )
}