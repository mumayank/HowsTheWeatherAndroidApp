package com.mumayank.howstheweather.main.famous_cities

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mumayank.howstheweather.R
import com.mumayank.howstheweather.main.details.data.CityWithCode

class RvAdapter(
    private val cities: List<CityWithCode>,
    private val onClick: ((cityWithCode: CityWithCode) -> Unit)?
) :
    RecyclerView.Adapter<RvAdapter.ViewHolder>() {

    class ViewHolder(
        itemView: View,
        private val onClick: ((cityWithCode: CityWithCode) -> Unit)?
    ) : RecyclerView.ViewHolder(itemView) {
        fun bindItems(cityWithCode: CityWithCode) {
            val clickableLayout = itemView.findViewById(R.id.clickable_layout) as LinearLayout
            val textViewName = itemView.findViewById(R.id.text_view_name) as TextView
            clickableLayout.setOnClickListener {
                onClick?.invoke(cityWithCode)
            }
            textViewName.text = cityWithCode.name
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.layout_rv_item_famous_cities, parent, false)
        return ViewHolder(v, onClick)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(cities[position])
    }

    override fun getItemCount(): Int {
        return cities.size
    }
}