package com.sostrovsky.onlineshop.remote.response

sealed class RemoteResponse <out T : Any> {
    data class Success<out T : Any>(val output : T) : RemoteResponse<T>()
    data class Error(val exception: Exception)  : RemoteResponse<Nothing>()
}