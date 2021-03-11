package com.mumayank.howstheweather.main.details

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mumayank.howstheweather.databinding.ActivityDetailsBinding

class DetailsActivity : AppCompatActivity() {

    companion object {
        const val IE_CITY_LAT = "IE_CITY_LAT"
        const val IE_CITY_LON = "IE_CITY_LON"
        const val IE_CITY_NAME = "IE_CITY_NAME"
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

        viewModel.getData(
            intent.getDoubleExtra(IE_CITY_LAT, 0.0),
            intent.getDoubleExtra(IE_CITY_LON, 0.0)
        )
        viewModel.multiDayForecast.observe(this) {
            if (it == null) {
                return@observe
            }
            val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplication())
            val isMetricUnitSystem = sharedPreferences.getBoolean("isMetricUnitSystem", true)
            binding.recyclerView.layoutManager =
                LinearLayoutManager(this, RecyclerView.VERTICAL, false)
            binding.recyclerView.adapter = RvAdapter(it.list, isMetricUnitSystem)
        }
        viewModel.isInProgress.observe(this) {
            binding.progressViewLayout.progressLayout.visibility = if (it) View.VISIBLE else View.GONE
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }
}