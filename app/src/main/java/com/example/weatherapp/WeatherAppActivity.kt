package com.example.weatherapp

import android.content.Context
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.location.LocationManagerCompat.isLocationEnabled
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.weatherapp.databinding.ActivityWeatherBinding
import com.example.weatherapp.ui.current_weather.CurrentWeatherFragment
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.util.*

class WeatherAppActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWeatherBinding
    private lateinit var checkNetworkConnection: CheckNetworkConnection
//    private lateinit var permissionLauncher: ActivityResultLauncher<String>
//    lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityWeatherBinding.inflate(layoutInflater)
        setContentView(binding.root)
        callNetworkConnection()

//        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
//        registerPermissionListener()
//        getCurrentLocation()

        val navView: BottomNavigationView = binding.bottomNavigation
        val navController =
            (supportFragmentManager.findFragmentById(R.id.nav_host_fragment)
                    as NavHostFragment).navController

        navView.setupWithNavController(navController)

    }

    private fun callNetworkConnection() {
        checkNetworkConnection = CheckNetworkConnection(application)
        checkNetworkConnection.observe(this, { isConnected ->
            if (isConnected) {
                Toast.makeText(applicationContext, "connect", Toast.LENGTH_SHORT).show()
                Log.d("yes", "Yes connection")
            } else {
                Toast.makeText(applicationContext, "No internet", Toast.LENGTH_SHORT).show()
                Log.d("no", "No connection")
                binding.internet.setBackgroundResource(R.drawable.ic_baseline_sunny)
            }
        })
    }

    private fun checkForInternet(): Boolean {

        // register activity with the connectivity manager service
        val connectivityManager =
            getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        // if the android version is equal to M
        // or greater we need to use the
        // NetworkCapabilities to check what type of
        // network has the internet connection
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            // Returns a Network object corresponding to
            // the currently active default data network.
            val network = connectivityManager.activeNetwork ?: return false

            // Representation of the capabilities of an active network.
            val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false

            return when {
                // Indicates this network uses a Wi-Fi transport,
                // or WiFi has network connectivity
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true

                // Indicates this network uses a Cellular transport. or
                // Cellular has network connectivity
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true

                // else return false
                else -> false
            }
        } else {
            // if the android version is below M
            @Suppress("DEPRECATION") val networkInfo =
                connectivityManager.activeNetworkInfo ?: return false
            @Suppress("DEPRECATION")
            return networkInfo.isConnected
        }
    }


//    private fun getCurrentLocation() {
//        if (checkPermissions()) {
//            getCoordinates()
//        } else {
//            permissionLauncher.launch(android.Manifest.permission.ACCESS_FINE_LOCATION)
//        }
//    }
//
//
//    private fun checkPermissions(): Boolean {
//        if (ContextCompat.checkSelfPermission(
//                applicationContext,
//                android.Manifest.permission.ACCESS_FINE_LOCATION
//            ) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
//                applicationContext,
//                android.Manifest.permission.ACCESS_COARSE_LOCATION
//            ) == PackageManager.PERMISSION_GRANTED
//        ) {
//            return true
//        }
//        return false
//    }
//
//    private fun registerPermissionListener() {
//        permissionLauncher =
//            registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
//                if (isGranted) {
//                    Log.i("DEBUG", "permission granted")
//                } else {
//                    Log.i("DEBUG", "permission denied")
//                }
//            }
//    }
//
//    private fun getCoordinates() {
//        val locationManager: LocationManager =
//            getSystemService(Context.LOCATION_SERVICE) as LocationManager
//        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
//            || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
//        ) {
//            if (ContextCompat.checkSelfPermission(
//                    applicationContext,
//                    android.Manifest.permission.ACCESS_FINE_LOCATION
//                ) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
//                    applicationContext,
//                    android.Manifest.permission.ACCESS_COARSE_LOCATION
//                ) != PackageManager.PERMISSION_GRANTED
//            ) {
//                Toast.makeText(
//                    applicationContext,
//                    "You need to allow use your location",
//                    Toast.LENGTH_SHORT
//                ).show()
//            }
//            fusedLocationProviderClient.lastLocation.addOnCompleteListener(this) { task ->
//                val locationRequest =
//                    com.google.android.gms.location.LocationRequest.create().apply {
//                        interval = 10000
//                        fastestInterval = 1000
//                        priority =
//                            com.google.android.gms.location.LocationRequest.PRIORITY_HIGH_ACCURACY
//                        maxWaitTime = 1000
//                        numUpdates = 2
//                    }
//                val geocoder = Geocoder(this, Locale.Builder().setLanguage("en").build())
//                var addresses: List<Address>
//                LocationServices.getFusedLocationProviderClient(this)
//                    .requestLocationUpdates(locationRequest, object : LocationCallback() {
//                        override fun onLocationResult(locationResult: LocationResult) {
//                            if (!locationResult.equals(0)) {
//                                super.onLocationResult(locationResult)
//                            }
//                            LocationServices.getFusedLocationProviderClient(this@WeatherAppActivity)
//                                .removeLocationUpdates(this)
//                            if (!locationResult.equals(0) && locationResult.locations.size > 0) {
//                                val locIndex = locationResult.locations.size - 1
//
//                                val latitude = locationResult.locations[locIndex].latitude
//                                val longitude = locationResult.locations[locIndex].longitude
//                                Log.d("lon", latitude.toString())
//                                Log.d("lat", longitude.toString())
//                                val bundle = Bundle()
//                                bundle.putDouble("lon", longitude)
////                                val myFragment = CurrentWeatherFragment()
////                                myFragment.arguments = bundle
//                               // addresses = geocoder.getFromLocation(latitude, longitude, 1)
//                              //  val address: String = addresses.first().countryName.toString()
//                                //Log.d("country", address)
//                            }
//                        }
//                    }, Looper.getMainLooper())
//            }
//
//        }
//    }
//
}