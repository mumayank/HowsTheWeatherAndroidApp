package com.mumayank.howstheweather.main.details

import android.app.Activity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mumayank.howstheweather.R
import com.mumayank.howstheweather.databinding.ActivityDetailsBinding

class DetailsActivity : AppCompatActivity() {

    companion object {
        const val IE_CITY_LAT = "IE_CITY_LAT"
        const val IE_CITY_LON = "IE_CITY_LON"
        const val IE_CITY_NAME = "IE_CITY_NAME"
        const val IE_CITY_ID = "IE_CITY_ID"

        fun getUnit(activity: Activity): String {
            val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(activity)
            val isMetricUnitSystem = sharedPreferences.getBoolean("unit", false)
            return if (isMetricUnitSystem) {
                "metric"
            } else {
                "imperial"
            }
        }
    }

    private lateinit var binding: ActivityDetailsBinding
    private val viewModel: DetailsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.toolbar.title = intent.getStringExtra(IE_CITY_NAME)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        viewModel.multiDayForecast.observe(this) {
            if (it == null) {
                return@observe
            }
            val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(application)
            val isMetricUnitSystem = sharedPreferences.getBoolean("unit", false)
            binding.recyclerView.layoutManager =
                LinearLayoutManager(this, RecyclerView.VERTICAL, false)
            binding.recyclerView.adapter = RvAdapter(it.list, isMetricUnitSystem)
        }
        viewModel.isInProgress.observe(this) {
            binding.progressViewLayout.progressLayout.visibility =
                if (it) View.VISIBLE else View.GONE
        }
        viewModel.hasErrorOccurred.postValue(false)
        viewModel.hasErrorOccurred.observe(this) {
            if (it) {
                Toast.makeText(this, getString(R.string.no_network), Toast.LENGTH_SHORT).show()
                finish()
            }
        }

        val lat = intent.getDoubleExtra(IE_CITY_LAT, -1.0)
        val lon = intent.getDoubleExtra(IE_CITY_LON, -1.0)
        val id = intent.getLongExtra(IE_CITY_ID, -1L)

        if (id != -1L) {
            viewModel.getData(id, getUnit(this))
        } else {
            viewModel.getData(lat, lon, getUnit(this))
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }
}