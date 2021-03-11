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

class DetailsViewModel(application: Application) : AndroidViewModel(application) {

    companion object {
        fun getUnit(application: Application): String {
            val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(application)
            val isMetricUnitSystem = sharedPreferences.getBoolean("isMetricUnitSystem", true)
            return if (isMetricUnitSystem) {
                "metric"
            } else {
                "imperial"
            }
        }

        const val filterTimeEndWith = "00:00:00"
    }

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
            val apiInterface = RetrofitFactory.getClient().create(RestApiService::class.java)
            val result = apiInterface.getMutidayForecast(latitude, longitude, getUnit(getApplication()))
            val response = result.body()!!
            filterData(response)
        }
    }

    fun getData(cityId: Long) {
        if (multiDayForecast.value != null) {
            return
        }
        isInProgress.postValue(true)
        viewModelScope.launch(Dispatchers.IO) {
            val apiInterface = RetrofitFactory.getClient().create(RestApiService::class.java)
            val result = apiInterface.getMutidayForecastWithId(cityId, getUnit(getApplication()))
            val response = result.body()!!
            filterData(response)
        }
    }

    private fun filterData(response: MultiDayForecast) {
        viewModelScope.launch(Dispatchers.IO) {
            val filteredResponse = arrayListOf<Lists>()
            for (i in response.list) {
                if (i.dt_txt.endsWith(filterTimeEndWith)) {
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