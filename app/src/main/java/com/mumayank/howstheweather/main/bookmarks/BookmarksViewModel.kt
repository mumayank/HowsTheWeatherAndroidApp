package com.mumayank.howstheweather.main.bookmarks

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.mumayank.howstheweather.db.City
import com.mumayank.howstheweather.db.Db
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class BookmarksViewModel(application: Application) : AndroidViewModel(application) {

    var cities = Db.getDb(application).cityDao().getAll().asLiveData()

    fun removeBookmarkedCity(city: City) {
        viewModelScope.launch(Dispatchers.IO) {
            Db.getDb(getApplication()).cityDao().delete(city)
        }
    }
}