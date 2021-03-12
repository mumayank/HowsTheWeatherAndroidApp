package com.mumayank.howstheweather.settings

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.mumayank.howstheweather.repository.repos.bookmark.BookmarkRepositoryFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SettingsViewModel(application: Application) : AndroidViewModel(application) {

    fun resetPrefs() {
        viewModelScope.launch(Dispatchers.IO) {
            BookmarkRepositoryFactory.get().deleteAll(getApplication())
        }
    }

}