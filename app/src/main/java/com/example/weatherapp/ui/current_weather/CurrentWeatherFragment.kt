package com.example.weatherapp.ui.current_weather

import android.app.AlertDialog
import android.app.Dialog
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
import androidx.core.view.isVisible
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import com.example.weatherapp.API_KEY
import com.example.weatherapp.R
import com.example.weatherapp.databinding.CurrentWeatherFragmentBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.util.*
import kotlin.collections.ArrayList
import kotlin.properties.Delegates

class CurrentWeatherFragment : Fragment() {

    private val viewModel: CurrentWeatherViewModel by viewModels()
    private lateinit var binding: CurrentWeatherFragmentBinding

    private lateinit var permissionLauncher: ActivityResultLauncher<String>
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        registerPermissionListener()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = CurrentWeatherFragmentBinding.inflate(inflater, container, false)
        fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(requireContext())
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getCurrentLocation()
        viewModel.weatherInfo.observe(viewLifecycleOwner) { weatherData ->
            with(binding) {
                weatherIcon.setBackgroundResource(
                    when (weatherData.weather[0].icon) {
                        "01n" -> R.drawable.moon
                        "01d" -> R.drawable.sun
                        "02d", "03d" -> R.drawable.cloudy_day
                        "02n", "03n" -> R.drawable.cloud_moon
                        "04d", "04n" -> R.drawable.clouds
                        "09d", "10d" -> R.drawable.rainy_day
                        "09n", "10n" -> R.drawable.rainy_night
                        "11d", "11n" -> R.drawable.thunderstorm
                        "13d", "13n" -> R.drawable.snow
                        "50d", "50n" -> R.drawable.fog
                        else -> R.drawable.default_weather
                    }
                )
                temperature.text = (weatherData.main.temp - 273.0).toInt().toString()
                skyDescription.text = weatherData.weather[0].description
                degrees.isVisible = true
                cloudDescription.text = weatherData.clouds.all.toString() + "%"
                humidityDescription.text = weatherData.main.humidity.toString() + "mm"
                pressureDescription.text = weatherData.main.pressure.toString() + "hPa"
                windSpeedDescription.text =
                    (weatherData.wind.speed * 3.6).toInt().toString() + "km/h"
                when (weatherData.wind.deg.toString().toInt()) {
                    in 0..44 -> windDirectionDescription.text = "N"
                    360 -> windDirectionDescription.text = "N"
                    in 45..89 -> windDirectionDescription.text = "NE"
                    in 90..134 -> windDirectionDescription.text = "E"
                    in 135..179 -> windDirectionDescription.text = "SE"
                    in 180..224 -> windDirectionDescription.text = "S"
                    in 225..269 -> windDirectionDescription.text = "SW"
                    in 270..314 -> windDirectionDescription.text = "W"
                    in 315..359 -> windDirectionDescription.text = "SW"
                }
                val sdf = SimpleDateFormat("dd.MM.yyyy hh:mm")
                val currentDate = sdf.format(Date())
                date.text = currentDate
                //countryName.text = weatherData.sys.country
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
            getCurrentLocation()
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
        Toast.makeText(context, "Please turn on location in settings", Toast.LENGTH_SHORT).show()
        return false
    }

    private fun registerPermissionListener() {
        permissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
                if (isGranted) {
                    Log.i("DEBUG", "permission granted")
                } else {
                    Toast.makeText(
                        context,
                        "Please turn on location in settings",
                        Toast.LENGTH_SHORT
                    ).show()
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
                                binding.progressBarLoading.isVisible = false
                                setCoordinates(Pair(latitude, longitude))
                                addresses = geocoder.getFromLocation(latitude, longitude, 1)
                                val address: String = addresses.first().countryName.toString()
                                Log.d("adress", address)
                                binding.countryName.text = address
                                setCountryName(address)
                            }
                        }
                    }, Looper.getMainLooper())
            }
        } else Toast.makeText(context, "Please turn on location", Toast.LENGTH_LONG).show()

    }

    private fun setCoordinates(coordinates: Pair<Double, Double>) {
        setFragmentResult("request_key_lon", bundleOf(("coordinates" to coordinates)))
    }

    private fun setCountryName(countryName: String) {
        setFragmentResult("request_key_country_name", bundleOf(("countryName" to countryName)))
    }

}