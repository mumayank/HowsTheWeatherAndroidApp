package com.mumayank.howstheweather.repository.db

import android.app.Application
import android.content.Context
import androidx.room.Room

object Db {
    private lateinit var db: AppDatabase

    fun getDb(context: Context): AppDatabase {
        if (this::db.isInitialized.not()) {
            db = Room.databaseBuilder(
                context,
                AppDatabase::class.java, "database-name"
            ).build()
        }
        return db
    }
}