package com.mumayank.howstheweather.main.bookmarks

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.mumayank.howstheweather.repository.db.Bookmark
import com.mumayank.howstheweather.repository.repos.bookmark.BookmarkRepositoryFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class BookmarksViewModel(application: Application) : AndroidViewModel(application) {

    var bookmarks = BookmarkRepositoryFactory.get().getAll(application)

    fun removeBookmark(bookmark: Bookmark) {
        viewModelScope.launch(Dispatchers.IO) {
            BookmarkRepositoryFactory.get().delete(getApplication(), bookmark)
        }
    }
}