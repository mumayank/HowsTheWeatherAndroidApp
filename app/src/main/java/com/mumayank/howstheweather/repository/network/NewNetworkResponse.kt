package com.mumayank.howstheweather.repository.network

import okhttp3.ResponseBody

sealed class NewNetworkResponse<T : ResponseBody?> {
    class Success<T>(t: T) : NewNetworkResponse<ResponseBody?>()
    class Failure<T> : NewNetworkResponse<ResponseBody?>()
}