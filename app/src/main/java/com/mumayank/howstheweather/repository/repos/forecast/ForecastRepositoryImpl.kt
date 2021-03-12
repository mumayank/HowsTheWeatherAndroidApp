package com.mumayank.howstheweather.repository.repos.forecast

import android.content.Context
import com.google.gson.Gson
import com.mumayank.howstheweather.main.details.data.MultiDayForecast
import com.mumayank.howstheweather.repository.network.NetworkHelper
import com.mumayank.howstheweather.repository.network.NetworkUrls
import okhttp3.ResponseBody

class ForecastRepositoryImpl : ForecastRepository {

    override suspend fun getMultiDayForecast(
        context: Context,
        latitude: Double,
        longitude: Double,
        unit: String
    ): MultiDayForecast? {
        val responseBody = NetworkHelper.makeApiCall(
            context, NetworkUrls.getMultiDayForecast(
                latitude, longitude,
                unit
            )
        )
        return processResponseBody(responseBody)
    }

    override suspend fun getMultiDayForecast(
        context: Context,
        cityId: Long,
        unit: String
    ): MultiDayForecast? {
        val responseBody = NetworkHelper.makeApiCall(
            context, NetworkUrls.getMultiDayForecast(
                cityId,
                unit
            )
        )
        return processResponseBody(responseBody)
    }

    private fun processResponseBody(responseBody: ResponseBody?): MultiDayForecast? {
        return if (responseBody == null) {
            null
        } else {
            Gson().fromJson(responseBody.string(), MultiDayForecast::class.java)
        }
    }

}