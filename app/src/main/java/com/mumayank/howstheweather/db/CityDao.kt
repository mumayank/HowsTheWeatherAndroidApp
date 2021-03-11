package com.mumayank.howstheweather.db

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface CityDao {
    @Query("SELECT * FROM city")
    fun getAll(): Flow<List<City>>

    @Query("SELECT * FROM city WHERE lat LIKE :latitude AND " +
            "lon LIKE :longitude LIMIT 1")
    suspend fun findByLatLon(latitude: Double, longitude: Double): City

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(vararg city: City)

    @Delete
    suspend fun delete(city: City)
}
