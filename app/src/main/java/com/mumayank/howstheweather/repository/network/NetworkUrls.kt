package com.mumayank.howstheweather.repository.network

object NetworkUrls {
    private const val BASE_URL = "https://api.openweathermap.org"

    fun getMultiDayForecast(lat: Double, lon: Double, units: String): String {
        return "$BASE_URL/data/2.5/forecast?lat=$lat&lon=$lon&units=$units"
    }

    fun getMultiDayForecast(id: Long, units: String): String {
        return "$BASE_URL/data/2.5/forecast?id=$id&units=$units"
    }

}