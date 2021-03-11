package com.mumayank.howstheweather.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.android.gms.maps.model.LatLng
import com.google.gson.annotations.SerializedName
import java.io.Serializable

@Entity(primaryKeys = ["name"])
data class City(
    val name: String,
    val lat: Double,
    val lon: Double
)