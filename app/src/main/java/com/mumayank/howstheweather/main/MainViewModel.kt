package com.mumayank.howstheweather.main

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.mumayank.howstheweather.main.bookmarks.data.SingleDayForecastResponse
import com.mumayank.howstheweather.network.RestApiService
import com.mumayank.howstheweather.network.RetrofitFactory
import kotlinx.coroutines.launch

class MainViewModel(application: Application): AndroidViewModel(application) {

    val singleDayForecastResponse: MutableLiveData<SingleDayForecastResponse> by lazy {
        MutableLiveData<SingleDayForecastResponse>(null)
    }

    fun getData() {
        viewModelScope.launch {
            val apiInterface = RetrofitFactory.getClient().create(RestApiService::class.java)
            val result = apiInterface.getTodaysForecast(35.0, 139.0)
            singleDayForecastResponse.postValue(result.body())
        }
    }

}