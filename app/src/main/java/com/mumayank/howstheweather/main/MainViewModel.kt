package com.mumayank.howstheweather.main

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.mumayank.howstheweather.R
import com.mumayank.howstheweather.main.details.data.CityWithCode
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.BufferedReader
import java.io.InputStreamReader
import java.util.*

class MainViewModel(application: Application) : AndroidViewModel(application) {

    companion object {
        const val file_line_delimiter = ","
    }

    val famousCities: MutableLiveData<List<CityWithCode>> by lazy {
        MutableLiveData<List<CityWithCode>>()
    }

    val isInProgress = MutableLiveData(true)

    fun updateFamousCities(searchTerm: String) {
        isInProgress.postValue(true)
        viewModelScope.launch(Dispatchers.IO) {
            val reader = BufferedReader(
                InputStreamReader(
                    getApplication<Application>().resources.openRawResource(
                        R.raw.top_cities
                    )
                )
            )
            val arrayList = arrayListOf<CityWithCode>()
            var line = reader.readLine()
            while (line != null) {
                val city = line.substringBefore(file_line_delimiter)
                val cityCode = line.substringAfter(file_line_delimiter)
                if (searchTerm.isBlank() || city.toLowerCase(Locale.getDefault())
                        .contains(searchTerm.toLowerCase(Locale.getDefault()))
                ) {
                    arrayList.add(CityWithCode(city, cityCode.toLong()))
                }
                line = reader.readLine()
            }
            arrayList.sortedWith(compareBy { it.name })
            viewModelScope.launch(Dispatchers.Main) {
                famousCities.postValue(arrayList)
                isInProgress.postValue(false)
            }
        }
    }

}