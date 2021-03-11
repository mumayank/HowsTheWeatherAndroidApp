package com.mumayank.howstheweather.main.bookmarks

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Adapter
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mumayank.howstheweather.R
import com.mumayank.howstheweather.db.City

class RvAdapter(
    private val cities: ArrayList<City>,
    private val onClick: ((city: City) -> Unit)?,
    private val onRemove: ((city: City) -> Unit)?
) :
    RecyclerView.Adapter<RvAdapter.ViewHolder>() {

    class ViewHolder(
        itemView: View,
        val onClick: ((city: City) -> Unit)?,
        val onRemove: ((city: City) -> Unit)?
    ) : RecyclerView.ViewHolder(itemView) {
        fun bindItems(city: City) {
            val imageViewRemoveItem =
                itemView.findViewById(R.id.image_view_remove_item) as ImageView
            val clickableLayout = itemView.findViewById(R.id.clickable_layout) as LinearLayout
            val textViewName = itemView.findViewById(R.id.text_view_name) as TextView
            imageViewRemoveItem.setOnClickListener {
                onRemove?.invoke(city)
            }
            clickableLayout.setOnClickListener {
                onClick?.invoke(city)
            }
            textViewName.text = city.name
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.bookmarks_rv_item_layout, parent, false)
        return ViewHolder(v, onClick, onRemove)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(cities[position])
    }

    override fun getItemCount(): Int {
        return cities.size
    }
}