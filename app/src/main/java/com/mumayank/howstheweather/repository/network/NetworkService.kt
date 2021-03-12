package com.mumayank.howstheweather.repository.network

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Url

interface NetworkService {
    @GET
    fun apiGet(
        @Url urlString: String
    ): Call<ResponseBody?>
}