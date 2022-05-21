package com.example.weatherapp.ui.current_weather

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.api.WeatherApiProvider
import com.example.weatherapp.data.current_weather.CurrentWeatherResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CurrentWeatherViewModel : ViewModel() {
    private val api = WeatherApiProvider.api
    private val _weatherInfo = MutableLiveData<CurrentWeatherResponse>()
    val weatherInfo: LiveData<CurrentWeatherResponse> = _weatherInfo

    fun getDataOfCountry(lat: Double, lon: Double, appid: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _weatherInfo.postValue(api.getWeather(lat,lon,appid))
            } catch (e: Exception) {
                Log.e("TAG", e.message.orEmpty())
            }
        }
    }
}