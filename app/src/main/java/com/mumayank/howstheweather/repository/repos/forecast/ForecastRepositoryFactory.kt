package com.mumayank.howstheweather.repository.repos.forecast

object ForecastRepositoryFactory {
    fun get(): ForecastRepository = ForecastRepositoryImpl()
}