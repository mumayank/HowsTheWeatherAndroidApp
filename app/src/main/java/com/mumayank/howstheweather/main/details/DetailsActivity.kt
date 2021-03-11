package com.mumayank.howstheweather.main.details

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.mumayank.howstheweather.R

class DetailsActivity : AppCompatActivity() {

    companion object {
        const val IE_CITY_LAT = "IE_CITY_LAT"
        const val IE_CITY_LON = "IE_CITY_LON"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)
    }
}