package com.currencyconverter.utils

sealed class  ResultReceiver<out T> {
    data class Success<out T>(val data: T) : ResultReceiver<T>()
    data object Loading : ResultReceiver<Nothing>()
    data class Error(val message: Throwable) : ResultReceiver<Nothing>()
}