package com.mumayank.howstheweather.network

import com.mumayank.howstheweather.main.bookmarks.data.SingleDayForecastResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface RestApiService {
    @GET("/data/2.5/weather")
    suspend fun getTodaysForcast(
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double
    ): Response<SingleDayForecastResponse>
}