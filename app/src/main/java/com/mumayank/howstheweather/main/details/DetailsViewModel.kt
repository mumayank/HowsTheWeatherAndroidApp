package com.mumayank.howstheweather.main.details

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.mumayank.howstheweather.main.bookmarks.data.SingleDayForecastResponse
import com.mumayank.howstheweather.network.RestApiService
import com.mumayank.howstheweather.network.RetrofitFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DetailsViewModel(application: Application): AndroidViewModel(application) {

    val singleDayForecastResponse: MutableLiveData<SingleDayForecastResponse> by lazy {
        MutableLiveData<SingleDayForecastResponse>(null)
    }

    val isInProgress = MutableLiveData<Boolean>(true)

    fun getData(latitude: Double, longitude: Double) {
        if (singleDayForecastResponse.value != null) {
            return
        }
        isInProgress.postValue(true)
        viewModelScope.launch(Dispatchers.IO) {
            val apiInterface = RetrofitFactory.getClient().create(RestApiService::class.java)
            val result = apiInterface.getTodaysForecast(latitude, longitude)
            singleDayForecastResponse.postValue(result.body()!!)
            viewModelScope.launch(Dispatchers.Main) {
                isInProgress.postValue(false)
            }
        }
    }

}