package com.mumayank.howstheweather.main.details

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.preference.PreferenceManager
import com.google.gson.Gson
import com.mumayank.howstheweather.main.details.data.Lists
import com.mumayank.howstheweather.main.details.data.MultiDayForecast
import com.mumayank.howstheweather.network.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

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
    val hasErrorOccurred = MutableLiveData<Boolean>(false)

    fun getData(latitude: Double, longitude: Double) {
        if (multiDayForecast.value != null) {
            return
        }
        getData(NetworkUrls.getMultiDayForecast(latitude, longitude, getUnit(getApplication())))
    }

    fun getData(cityId: Long) {
        if (multiDayForecast.value != null) {
            return
        }
        getData(NetworkUrls.getMultiDayForecast(cityId, getUnit(getApplication())))
    }

    private fun getData(url: String) {
        NetworkHelper.makeApiCall(
            getApplication(),
            url,
            object : NetworkCallback {
                override fun onFailure() {
                    hasErrorOccurred.postValue(true)
                }

                override fun onSuccess(responseBody: ResponseBody?) {
                    if (responseBody == null) {
                        hasErrorOccurred.postValue(true)
                    } else {
                        isInProgress.postValue(true)
                        processResponse(responseBody.string())
                    }
                }
            })
    }

    private fun processResponse(string: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val multiDayForecast = Gson().fromJson(string, MultiDayForecast::class.java)
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