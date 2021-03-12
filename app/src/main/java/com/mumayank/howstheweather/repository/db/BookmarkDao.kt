package com.mumayank.howstheweather.repository.db

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface BookmarkDao {
    @Query("SELECT * FROM bookmark")
    fun getAll(): Flow<List<Bookmark>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(bookmark: Bookmark)

    @Delete
    suspend fun delete(bookmark: Bookmark)

    @Query("DELETE FROM bookmark")
    suspend fun deleteAll()
}
