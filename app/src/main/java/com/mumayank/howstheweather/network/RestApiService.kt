package com.mumayank.howstheweather.network

import com.mumayank.howstheweather.main.details.data.MultiDayForecast
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface RestApiService {
    /*@GET("/data/2.5/weather")
    suspend fun getTodaysForecast(
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double,
        @Query("units") units: String
    ): Response<SingleDayForecastResponse>*/

    @GET("/data/2.5/forecast")
    suspend fun getMutidayForecast(
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double,
        @Query("units") units: String
    ): Response<MultiDayForecast>

    @GET("/data/2.5/forecast")
    suspend fun getMutidayForecastWithId(
        @Query("id") id: Long,
        @Query("units") units: String
    ): Response<MultiDayForecast>
}