package com.mumayank.howstheweather.db

import androidx.room.Entity

@Entity(primaryKeys = ["name"])
data class City(
    val name: String,
    val lat: Double,
    val lon: Double
)