package com.mumayank.howstheweather.maps

import android.app.Application
import android.location.Geocoder
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*

class MapsViewModel(application: Application): AndroidViewModel(application) {

    val currentCity: MutableLiveData<City?> by lazy {
        MutableLiveData<City?>()
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
            currentCity.postValue(null)
            isInProgress.postValue(false)
        }
    }

    private suspend fun setCityNameSearchSuccessResult(cityName: String, latLng: LatLng) {
        viewModelScope.launch(Dispatchers.Main) {
            currentCity.postValue(City(cityName, latLng))
            isInProgress.postValue(false)
        }
    }

}