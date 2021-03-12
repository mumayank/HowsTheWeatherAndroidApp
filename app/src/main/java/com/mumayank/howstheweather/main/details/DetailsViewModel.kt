package com.mumayank.howstheweather.main.details

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.preference.PreferenceManager
import com.mumayank.howstheweather.main.details.data.Lists
import com.mumayank.howstheweather.main.details.data.MultiDayForecast
import com.mumayank.howstheweather.repository.repos.forecast.ForecastRepositoryFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DetailsViewModel(application: Application) : AndroidViewModel(application) {

    companion object {
        fun getUnit(context: Context): String {
            val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
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
    val hasErrorOccurred = MutableLiveData<Boolean>(false)

    fun getData(latitude: Double, longitude: Double) {
        if (multiDayForecast.value != null) {
            return
        }
        viewModelScope.launch(Dispatchers.IO) {
            val multiDayForecast = ForecastRepositoryFactory.get().getMultiDayForecast(
                getApplication(),
                latitude,
                longitude,
                getUnit(getApplication())
            )
            processData(multiDayForecast)
        }
    }

    fun getData(cityId: Long) {
        if (multiDayForecast.value != null) {
            return
        }
        viewModelScope.launch(Dispatchers.IO) {
            val multiDayForecast = ForecastRepositoryFactory.get()
                .getMultiDayForecast(getApplication(), cityId, getUnit(getApplication()))
            processData(multiDayForecast)
        }
    }

    private fun processData(multiDayForecast: MultiDayForecast?) {
        if (multiDayForecast == null) {
            viewModelScope.launch(Dispatchers.Main) {
                hasErrorOccurred.postValue(true)
            }
        } else {
            filterData(multiDayForecast)
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