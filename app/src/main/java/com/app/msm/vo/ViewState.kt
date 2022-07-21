package com.app.msm.vo

sealed class ViewState<out T> {
    object Loading : ViewState<Nothing>()
    data class Error(val e: Throwable) : ViewState<Nothing>()
    data class Success<T>(val data: T) : ViewState<T>()
}
