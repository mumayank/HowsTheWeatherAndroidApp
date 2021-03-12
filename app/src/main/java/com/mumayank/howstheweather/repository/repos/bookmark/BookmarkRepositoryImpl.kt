package com.mumayank.howstheweather.repository.repos.bookmark

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.mumayank.howstheweather.repository.db.Bookmark
import com.mumayank.howstheweather.repository.db.Db

class BookmarkRepositoryImpl : BookmarkRepository {

    override fun getAll(context: Context): LiveData<List<Bookmark>> {
        return Db.getDb(context).bookmarkDao().getAll().asLiveData()
    }

    override suspend fun insert(context: Context, bookmark: Bookmark) {
        Db.getDb(context).bookmarkDao().insert(bookmark)
    }

    override suspend fun delete(context: Context, bookmark: Bookmark) {
        Db.getDb(context).bookmarkDao().delete(bookmark)
    }

    override suspend fun deleteAll(context: Context) {
        Db.getDb(context).bookmarkDao().deleteAll()
    }

}