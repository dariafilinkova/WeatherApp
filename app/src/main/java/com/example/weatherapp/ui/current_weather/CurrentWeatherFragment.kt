package com.example.weatherapp.ui.current_weather

import android.content.Context
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import com.example.weatherapp.API_KEY
import com.example.weatherapp.R
import com.example.weatherapp.databinding.CurrentWeatherFragmentBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import java.util.*
import kotlin.collections.ArrayList
import kotlin.properties.Delegates

class CurrentWeatherFragment : Fragment() {

    private val viewModel: CurrentWeatherViewModel by viewModels()
    private lateinit var binding: CurrentWeatherFragmentBinding

    private lateinit var permissionLauncher: ActivityResultLauncher<String>
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = CurrentWeatherFragmentBinding.inflate(inflater, container, false)
        registerPermissionListener()
        fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(requireContext())

//        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireContext())
//        registerPermissionListener()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getCurrentLocation()
        //  val latitude = getLatitude()
        //  getCurrentLocation()
//        val lat = arguments!!.getDouble("lon")
        // Log.d("latt", latitude.toString())
//        viewModel.getDataOfCountry(
//            arguments!!.getDouble("lat"),
//            arguments!!.getDouble("lon"),
//            API_KEY
//        )
//
        viewModel.weatherInfo.observe(viewLifecycleOwner) { countryData ->
            with(binding) {
                //countryName.text="ffff"
                countryName.text = countryData.id.toString()
//                weatherIcon.apply {
//                    when (countryData.weather.icon) {
//                        "01n" -> R.drawable.ic_baseline_sunny
//                    }
//                }
                Log.d("icon", countryData.weather[0].icon)
//                if(countryData.weather[0].icon == "01n" ){
//                weatherIcon.setBackgroundResource(R.drawable.ic_baseline_sunny)}
                weatherIcon.setBackgroundResource(
                    when (countryData.weather[0].icon) {
                        "01n" -> R.drawable.ic_baseline_sunny
                        "02n" -> R.drawable.ic_baseline_home
                        else -> R.drawable.ic_baseline_home
                    }
                )
                // countryName.text = countryData.sys.country
                //countryName.text=countryData.main.temp_max.toString()
            }
        }

    }

    companion object {
        @JvmStatic
        fun newInstance() = CurrentWeatherFragment()
    }

    private fun getCurrentLocation() {
        if (checkPermissions()) {
            getCoordinates()
        } else {
            permissionLauncher.launch(android.Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }


    private fun checkPermissions(): Boolean {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                requireContext(),
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            return true
        }
        return false
    }

    private fun registerPermissionListener() {
        permissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
                if (isGranted) {
                    Log.i("DEBUG", "permission granted")
                } else {
                    Log.i("DEBUG", "permission denied")
                }
            }
    }

    private fun getCoordinates() {
        val locationManager: LocationManager =
            activity?.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
            || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
        ) {
            if (ContextCompat.checkSelfPermission(
                    requireContext(),
                    android.Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                    requireContext(),
                    android.Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                Toast.makeText(
                    requireContext(),
                    "You need to allow use your location",
                    Toast.LENGTH_SHORT
                ).show()
            }
            fusedLocationProviderClient.lastLocation.addOnCompleteListener(requireActivity()) { task ->
                val locationRequest =
                    com.google.android.gms.location.LocationRequest.create().apply {
                        interval = 10000
                        fastestInterval = 1000
                        priority =
                            com.google.android.gms.location.LocationRequest.PRIORITY_HIGH_ACCURACY
                        maxWaitTime = 1000
                        numUpdates = 2
                    }
                val geocoder = Geocoder(activity, Locale.Builder().setLanguage("en").build())
                var addresses: List<Address>
                LocationServices.getFusedLocationProviderClient(requireActivity())
                    .requestLocationUpdates(locationRequest, object : LocationCallback() {
                        override fun onLocationResult(locationResult: LocationResult) {
                            if (!locationResult.equals(0)) {
                                super.onLocationResult(locationResult)
                            }
                            LocationServices.getFusedLocationProviderClient(requireActivity())
                                .removeLocationUpdates(this)
                            if (!locationResult.equals(0) && locationResult.locations.size > 0) {
                                val locIndex = locationResult.locations.size - 1

                                val latitude = locationResult.locations[locIndex].latitude
                                val longitude = locationResult.locations[locIndex].longitude
                                Log.d("lat", latitude.toString())
                                Log.d("lon", longitude.toString())
                                viewModel.getDataOfCountry(latitude, longitude, API_KEY)
//                                setCoordinates(longitude,latitude)
//                                val (a,b) = Pair(latitude,longitude)
//                                coordinates.add(latitude)
//                                coordinates.add(longitude)
                                setCoordinates(Pair(latitude,longitude))
                                //coordinates.set(1,latitude)
                                //addresses = geocoder.getFromLocation(latitude.toString(), longitude, 1)
                                //val address: String = addresses.first().countryName.toString()
                                //Log.d("country", address)
                            }
                        }
                    }, Looper.getMainLooper())
            }
        }

    }

    private fun setCoordinates(coor : Pair<Double,Double>) {
        setFragmentResult("request_key_lon", bundleOf(("coor" to coor)))
       //setFragmentResult("request_key_lat", bundleOf(("lat" to lat)))
    }

}