package com.mumayank.howstheweather.main.details

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mumayank.howstheweather.R
import com.mumayank.howstheweather.main.details.data.ImperialUnits
import com.mumayank.howstheweather.main.details.data.Lists
import com.mumayank.howstheweather.main.details.data.MetricUnits
import java.text.SimpleDateFormat
import java.util.*

class RvAdapter(private val lists: List<Lists>, private val isMetricUnitSystem: Boolean) :
    RecyclerView.Adapter<RvAdapter.ViewHolder>() {

    companion object {
        fun getTemperatureString(list: Lists, isMetricUnitSystem: Boolean): String {
            return "${list.main.temp} ${if (isMetricUnitSystem) MetricUnits.temperature else ImperialUnits.temperature}"
        }

        fun getHumidityString(list: Lists): String {
            return "${list.main.humidity} %"
        }

        fun getRainChancesString(list: Lists): String {
            return list.weather[0].description
        }

        fun getWindString(list: Lists, isMetricUnitSystem: Boolean): String {
            return "${list.wind.speed} ${if (isMetricUnitSystem) MetricUnits.windSpeed else ImperialUnits.windSpeed}"
        }

        private const val receivedDateFormat = "yyyy-MM-dd hh:mm:ss"
        private const val dateFormat = "dd MMM yyyy"
        fun getTitle(list: Lists): String {
            val simpleDateFormat = SimpleDateFormat(receivedDateFormat, Locale.getDefault())
            val date = simpleDateFormat.parse(list.dt_txt)
            val simpleDateFormatDesired = SimpleDateFormat(dateFormat, Locale.getDefault())
            return simpleDateFormatDesired.format(date!!)
        }
    }

    class ViewHolder(
        itemView: View,
        private val isMetricUnitSystem: Boolean
    ) : RecyclerView.ViewHolder(itemView) {
        fun bindItems(list: Lists) {
            val textViewTemperature = itemView.findViewById(R.id.text_view_temperature) as TextView
            textViewTemperature.text = getTemperatureString(list, isMetricUnitSystem)
            val textViewHumidity = itemView.findViewById(R.id.text_view_humidity) as TextView
            textViewHumidity.text = getHumidityString(list)
            val textViewRainChances = itemView.findViewById(R.id.text_view_rain_chances) as TextView
            textViewRainChances.text = getRainChancesString(list)
            val textViewWind = itemView.findViewById(R.id.text_view_wind_information) as TextView
            textViewWind.text = getWindString(list, isMetricUnitSystem)
            val textViewTitle = itemView.findViewById(R.id.text_view_title) as TextView
            textViewTitle.text = getTitle(list)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RvAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.layout_rv_item_details, parent, false)
        return RvAdapter.ViewHolder(v, isMetricUnitSystem)
    }

    override fun onBindViewHolder(holder: RvAdapter.ViewHolder, position: Int) {
        holder.bindItems(lists[position])
    }

    override fun getItemCount(): Int {
        return lists.size
    }
}