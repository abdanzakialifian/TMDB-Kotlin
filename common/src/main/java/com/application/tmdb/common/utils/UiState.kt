package com.application.tmdb.common.utils

sealed class UiState<out T> {
    data class Success<out T>(val data: T) : UiState<T>()
    data class Error(val errorMessage: String) : UiState<Nothing>()
    data class Loading<out T>(val data: T?) : UiState<T>()
    data object Empty : UiState<Nothing>()
}