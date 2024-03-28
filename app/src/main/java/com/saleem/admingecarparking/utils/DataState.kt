package com.saleem.admingecarparking.utils

sealed class DataState<out T> {
    data class Success<T>(val data: T? = null) : DataState<T>()
    data class Error(val errorMessage: String) : DataState<Nothing>()
    data object Loading : DataState<Nothing>()
}
