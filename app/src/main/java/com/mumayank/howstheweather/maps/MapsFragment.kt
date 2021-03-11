package com.mumayank.howstheweather.maps

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
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
            googleMap?.setPadding(100, 100, 100, 100)
            googleMap?.uiSettings?.isMapToolbarEnabled = false
            googleMap?.setOnMapClickListener { latLng ->
                viewModel.getCityName(latLng)
            }
            if (viewModel.currentCity.value != null) {
                updateCityUi(viewModel.currentCity.value!!)
            }
        }

        viewModel.currentCity.observe(viewLifecycleOwner) {
            if (googleMap == null) {
                return@observe
            }
            if (it == null) {
                Snackbar.make(
                    binding.coordinatorLayout,
                    "Could not detect a city in this region.\n(Hint: change zoom and try again)",
                    Snackbar.LENGTH_INDEFINITE
                ).show()
            } else {
                updateCityUi(it)
            }
        }

        viewModel.isInProgress.observe(viewLifecycleOwner) {
            binding.progressHolderLayout.progressLayout.visibility = if (it) View.VISIBLE else View.GONE
        }
    }

    private fun updateCityUi(city: City) {
        if (marker == null) {
            marker = googleMap?.addMarker(MarkerOptions().position(city.latLng))
            binding.textViewSelectACity.visibility = View.GONE
            binding.linearLayoutCitySelected.visibility = View.VISIBLE
        }
        marker?.position = city.latLng
        marker?.title = city.name
        binding.textViewSelectedCity.text = city.name
        googleMap?.animateCamera(CameraUpdateFactory.newLatLng(city.latLng))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}