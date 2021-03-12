package com.mumayank.howstheweather.repository.repos.forecast

import android.content.Context
import com.mumayank.howstheweather.main.details.data.MultiDayForecast

interface ForecastRepository {

    suspend fun getMultiDayForecast(
        context: Context,
        latitude: Double,
        longitude: Double,
        unit: String
    ): MultiDayForecast?

    suspend fun getMultiDayForecast(
        context: Context,
        cityId: Long,
        unit: String
    ): MultiDayForecast?

}