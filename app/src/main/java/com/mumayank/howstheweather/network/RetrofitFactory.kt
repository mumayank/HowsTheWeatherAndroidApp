package com.mumayank.howstheweather.network

import com.mumayank.howstheweather.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitFactory {
    fun getClient(): Retrofit {
        val interceptor = HttpLoggingInterceptor()
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        val client: OkHttpClient =
            OkHttpClient.Builder().addInterceptor(interceptor).addInterceptor {
                val original = it.request()
                val originalHttpUrl = original.url
                val url = originalHttpUrl.newBuilder()
                    .addQueryParameter("appid", BuildConfig.open_weather_map_api_key)
                    .build()
                val requestBuilder = original.newBuilder().url(url)
                val request = requestBuilder.build()
                it.proceed(request)
            }.build()
        return Retrofit.Builder()
            .baseUrl("https://api.openweathermap.org")
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
    }
}