package com.mumayank.howstheweather.main.bookmarks

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mumayank.howstheweather.R
import com.mumayank.howstheweather.repository.db.Bookmark

class RvAdapter(
    private val bookmarks: List<Bookmark>,
    private val onClick: ((bookmark: Bookmark) -> Unit)?,
    private val onRemove: ((bookmark: Bookmark) -> Unit)?
) :
    RecyclerView.Adapter<RvAdapter.ViewHolder>() {

    class ViewHolder(
        itemView: View,
        private val onClick: ((bookmark: Bookmark) -> Unit)?,
        private val onRemove: ((bookmark: Bookmark) -> Unit)?
    ) : RecyclerView.ViewHolder(itemView) {
        fun bindItems(bookmark: Bookmark) {
            val imageViewRemoveItem =
                itemView.findViewById(R.id.image_view_remove_item) as ImageView
            val clickableLayout = itemView.findViewById(R.id.clickable_layout) as LinearLayout
            val textViewName = itemView.findViewById(R.id.text_view_name) as TextView
            imageViewRemoveItem.setOnClickListener {
                onRemove?.invoke(bookmark)
            }
            clickableLayout.setOnClickListener {
                onClick?.invoke(bookmark)
            }
            textViewName.text = bookmark.cityName
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.layout_rv_item_bookmarks, parent, false)
        return ViewHolder(v, onClick, onRemove)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(bookmarks[position])
    }

    override fun getItemCount(): Int {
        return bookmarks.size
    }
}