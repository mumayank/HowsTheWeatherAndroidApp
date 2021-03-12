package com.mumayank.howstheweather.repository.db

import androidx.room.Entity

@Entity(primaryKeys = ["cityName"])
data class Bookmark(
    val cityName: String,
    val cityLat: Double,
    val cityLon: Double
)