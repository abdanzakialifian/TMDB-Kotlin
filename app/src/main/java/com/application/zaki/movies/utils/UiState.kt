package com.application.zaki.movies.utils

sealed class UiState<out T> {
    data class Success<out T>(val data: T) : UiState<T>()
    data class Error(val errorMessage: String) : UiState<Nothing>()
    data class Loading<out T>(val data: T?) : UiState<T>()
    object Empty : UiState<Nothing>()
}