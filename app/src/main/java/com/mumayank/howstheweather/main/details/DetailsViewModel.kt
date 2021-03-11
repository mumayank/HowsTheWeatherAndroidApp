package com.mumayank.howstheweather.main.details

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.preference.PreferenceManager
import com.mumayank.howstheweather.main.details.data.Lists
import com.mumayank.howstheweather.main.details.data.MultiDayForecast
import com.mumayank.howstheweather.network.RestApiService
import com.mumayank.howstheweather.network.RetrofitFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DetailsViewModel(application: Application): AndroidViewModel(application) {

    val multiDayForecast: MutableLiveData<MultiDayForecast> by lazy {
        MutableLiveData<MultiDayForecast>(null)
    }

    val isInProgress = MutableLiveData<Boolean>(true)

    fun getData(latitude: Double, longitude: Double) {
        if (multiDayForecast.value != null) {
            return
        }
        isInProgress.postValue(true)
        viewModelScope.launch(Dispatchers.IO) {
            val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplication())
            val isMetricUnitSystem = sharedPreferences.getBoolean("isMetricUnitSystem", true)
            val units = if (isMetricUnitSystem) {
                "metric"
            } else {
                "imperial"
            }

            val apiInterface = RetrofitFactory.getClient().create(RestApiService::class.java)
            val result = apiInterface.getMutidayForecast(latitude, longitude, units)
            val response = result.body()!!
            val filteredResponse = arrayListOf<Lists>()
            for (i in response.list) {
                if (i.dt_txt.endsWith("00:00:00")) {
                    filteredResponse.add(i)
                }
            }
            response.list.clear()
            response.list.addAll(filteredResponse)
            multiDayForecast.postValue(response)
            viewModelScope.launch(Dispatchers.Main) {
                isInProgress.postValue(false)
            }
        }
    }

}