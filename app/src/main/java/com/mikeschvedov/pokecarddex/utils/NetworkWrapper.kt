package com.mikeschvedov.pokecarddex.utils

sealed class NetworkWrapper<T> (val data: T? = null, val message: String? = null){
    class Success<T>(data: T): NetworkWrapper<T>(data)
    class Error<T>(message: String, data: T? = null) : NetworkWrapper<T>(data, message)
}