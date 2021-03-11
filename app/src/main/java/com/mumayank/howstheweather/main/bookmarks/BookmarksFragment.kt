package com.mumayank.howstheweather.main.bookmarks

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mumayank.howstheweather.databinding.FragmentBookmarksBinding
import com.mumayank.howstheweather.db.City
import com.mumayank.howstheweather.main.details.DetailsActivity

class BookmarksFragment : Fragment() {

    private var _binding: FragmentBookmarksBinding? = null
    private val binding get() = _binding!!
    private val viewModel: BookmarksViewModel by viewModels()
    private val cities = arrayListOf<City>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentBookmarksBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.recyclerView.layoutManager =
            LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
        binding.recyclerView.adapter = RvAdapter(cities, onClick = {
            startDetailsActivity(it)
        }, onRemove = {
            viewModel.removeBookmarkedCity(it)
        })
        viewModel.cities.observe(viewLifecycleOwner) {
            cities.clear()
            cities.addAll(it)
            binding.recyclerView.adapter!!.notifyDataSetChanged()

            if (cities.size == 0) {
                binding.emptyView.visibility = View.VISIBLE
            } else {
                binding.emptyView.visibility = View.GONE
            }
        }
    }

    private fun startDetailsActivity(city: City) {
        startActivity(
            Intent(
                activity,
                DetailsActivity::class.java
            ).putExtra(DetailsActivity.IE_CITY_LAT, city.lat)
                .putExtra(DetailsActivity.IE_CITY_LON, city.lon)
                .putExtra(DetailsActivity.IE_CITY_NAME, city.name)
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}