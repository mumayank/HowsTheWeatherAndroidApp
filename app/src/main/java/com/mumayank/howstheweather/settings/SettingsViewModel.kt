package com.mumayank.howstheweather.settings

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.mumayank.howstheweather.db.Db
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SettingsViewModel(application: Application) : AndroidViewModel(application) {

    fun resetPrefs() {
        viewModelScope.launch(Dispatchers.IO) {
            Db.getDb(getApplication()).cityDao().deleteAll()
        }
    }

}