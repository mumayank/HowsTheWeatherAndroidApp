package com.mumayank.howstheweather.main.maps

import android.app.Application
import android.location.Geocoder
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import com.mumayank.howstheweather.repository.db.Bookmark
import com.mumayank.howstheweather.repository.repos.bookmark.BookmarkRepositoryFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

class MapsViewModel(application: Application) : AndroidViewModel(application) {

    val currentMapCity: MutableLiveData<MapCity?> by lazy {
        MutableLiveData<MapCity?>()
    }

    val isInProgress = MutableLiveData(false)

    fun getCityName(latLng: LatLng) {
        isInProgress.postValue(true)
        viewModelScope.launch(Dispatchers.IO) {
            val geocoder = Geocoder(getApplication(), Locale.getDefault())
            val addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1)
            if (addresses.size > 0) {
                val cityName = addresses[0].locality ?: ""
                if (cityName.isBlank()) {
                    setCityNameSearchFailedResult()
                } else {
                    setCityNameSearchSuccessResult(cityName, latLng)
                }
            } else {
                setCityNameSearchFailedResult()
            }
        }
    }

    private suspend fun setCityNameSearchFailedResult() {
        viewModelScope.launch(Dispatchers.Main) {
            currentMapCity.postValue(null)
            isInProgress.postValue(false)
        }
    }

    private fun setCityNameSearchSuccessResult(cityName: String, latLng: LatLng) {
        viewModelScope.launch(Dispatchers.Main) {
            currentMapCity.postValue(MapCity(cityName, latLng))
            isInProgress.postValue(false)
        }
    }

    fun bookmarkCity() {
        viewModelScope.launch(Dispatchers.IO) {
            BookmarkRepositoryFactory.get().insert(
                getApplication(), Bookmark(
                    currentMapCity.value!!.name,
                    currentMapCity.value!!.latLng.latitude,
                    currentMapCity.value!!.latLng.longitude
                )
            )
        }
    }

}