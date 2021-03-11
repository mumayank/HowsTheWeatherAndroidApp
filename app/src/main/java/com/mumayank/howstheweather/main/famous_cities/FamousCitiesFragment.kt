package com.mumayank.howstheweather.main.famous_cities

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mumayank.howstheweather.main.MainViewModel
import com.mumayank.howstheweather.databinding.FragmentKnownCitiesBinding
import com.mumayank.howstheweather.db.City
import com.mumayank.howstheweather.main.bookmarks.RvAdapter
import com.mumayank.howstheweather.main.details.DetailsActivity
import com.mumayank.howstheweather.main.details.data.CityWithCode

class FamousCitiesFragment : Fragment() {

    private var _binding: FragmentKnownCitiesBinding? = null
    private val binding get() = _binding!!
    private val activityViewModel: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentKnownCitiesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        activityViewModel.updateFamousCities(binding.editTextSearch.text.toString())

        activityViewModel.famousCities.observe(viewLifecycleOwner) { cityWithCodeList ->
            if (cityWithCodeList == null) {
                return@observe
            }
            binding.recyclerView.layoutManager =
                LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
            binding.recyclerView.adapter = RvAdapter(cityWithCodeList, onClick = {
                startDetailsActivity(it)
            })
        }

        activityViewModel.isInProgress.observe(viewLifecycleOwner) {
            binding.progressLayout.progressLayout.visibility = if (it) View.VISIBLE else View.GONE
        }

        binding.editTextSearch.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // do nothing
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // do nothing
            }

            override fun afterTextChanged(s: Editable?) {
                activityViewModel.updateFamousCities(binding.editTextSearch.text.toString())
            }
        })
    }

    private fun startDetailsActivity(cityWithCode: CityWithCode) {
        startActivity(
            Intent(
                activity,
                DetailsActivity::class.java
            ).putExtra(DetailsActivity.IE_CITY_NAME, cityWithCode.name)
            .putExtra(DetailsActivity.IE_CITY_ID, cityWithCode.code)
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}