package com.mumayank.howstheweather.db

import android.app.Application
import androidx.room.Room

object Db {
    private lateinit var db: AppDatabase

    fun getDb(applicationContext: Application): AppDatabase {
        if (this::db.isInitialized.not()) {
            db = Room.databaseBuilder(
                applicationContext,
                AppDatabase::class.java, "database-name"
            ).build()
        }
        return db
    }
}