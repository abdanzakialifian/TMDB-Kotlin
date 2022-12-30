package com.application.zaki.movies.utils

sealed class NetworkResult<out T> {
    data class Success<out T>(val data: T) : NetworkResult<T>()
    data class Error(val errorMessage: String) : NetworkResult<Nothing>()
    data class Loading<out T>(val data: T?) : NetworkResult<T>()
    object Empty : NetworkResult<Nothing>()
}