package com.mumayank.howstheweather.main.maps

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.snackbar.Snackbar
import com.mumayank.howstheweather.R
import com.mumayank.howstheweather.databinding.FragmentMapsBinding
import java.util.*

class MapsFragment : Fragment() {

    private var _binding: FragmentMapsBinding? = null
    private val binding get() = _binding!!
    private var googleMap: GoogleMap? = null
    private var marker: Marker? = null
    private val viewModel: MapsViewModel by viewModels()

    companion object {
        @JvmStatic
        fun newInstance() =
            MapsFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMapsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment).getMapAsync {
            googleMap = it
            googleMap?.uiSettings?.isMapToolbarEnabled = false
            googleMap?.uiSettings?.isZoomControlsEnabled = true
            googleMap?.setOnMapClickListener { latLng ->
                viewModel.getCityName(latLng)
            }
            if (viewModel.currentMapCity.value != null) {
                updateCityUi(viewModel.currentMapCity.value!!)
            }
        }

        binding.buttonBookmark.setOnClickListener {
            viewModel.bookmarkCity()
            binding.linearLayoutCitySelected.visibility = View.GONE
            binding.textViewSelectACity.visibility = View.VISIBLE
            marker!!.remove()
            marker = null
            Snackbar.make(
                binding.coordinatorLayout,
                getString(R.string.city_bookmarked),
                Snackbar.LENGTH_LONG
            ).show()
        }

        viewModel.currentMapCity.observe(viewLifecycleOwner) {
            if (googleMap == null) {
                return@observe
            }
            if (it == null) {
                Snackbar.make(
                    binding.coordinatorLayout,
                    getString(R.string.could_not_detect_city),
                    Snackbar.LENGTH_LONG
                ).show()
            } else {
                updateCityUi(it)
            }
        }

        viewModel.isInProgress.observe(viewLifecycleOwner) {
            binding.progressHolderLayout.progressLayout.visibility =
                if (it) View.VISIBLE else View.GONE
        }
    }

    private fun updateCityUi(mapCity: MapCity) {
        if (marker == null) {
            marker = googleMap?.addMarker(MarkerOptions().position(mapCity.latLng))
            binding.textViewSelectACity.visibility = View.GONE
            binding.linearLayoutCitySelected.visibility = View.VISIBLE
        }
        marker?.position = mapCity.latLng
        marker?.title = mapCity.name
        binding.textViewSelectedCity.text = mapCity.name
        googleMap?.animateCamera(CameraUpdateFactory.newLatLng(mapCity.latLng))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}