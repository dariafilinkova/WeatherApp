package com.example.weatherapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.weatherapp.databinding.ActivityWeatherBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class WeatherAppActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWeatherBinding
    private lateinit var checkNetworkConnection: CheckNetworkConnection

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityWeatherBinding.inflate(layoutInflater)
        setContentView(binding.root)
        callNetworkConnection()

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
            }
        })
    }
}