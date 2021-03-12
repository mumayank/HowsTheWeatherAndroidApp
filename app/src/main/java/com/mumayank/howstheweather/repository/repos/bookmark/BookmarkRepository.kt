package com.mumayank.howstheweather.repository.repos.bookmark

import android.content.Context
import androidx.lifecycle.LiveData
import com.mumayank.howstheweather.repository.db.Bookmark

interface BookmarkRepository {
    fun getAll(context: Context): LiveData<List<Bookmark>>

    suspend fun insert(context: Context, bookmark: Bookmark)

    suspend fun delete(context: Context, bookmark: Bookmark)

    suspend fun deleteAll(context: Context)
}