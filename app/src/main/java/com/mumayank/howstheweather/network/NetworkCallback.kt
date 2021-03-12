package com.mumayank.howstheweather.network

import okhttp3.ResponseBody

@JvmSuppressWildcards
interface NetworkCallback {
    fun onFailure()
    fun onSuccess(responseBody: ResponseBody?)
}