package com.example.weatherapp.ui.forecast

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.api.WeatherApiProvider
import com.example.weatherapp.data.forecast.Forecast
import com.example.weatherapp.data.forecast.ForecastResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ForecastViewModel : ViewModel() {
    private val _forecastInfo = MutableLiveData<List<Forecast>>()
    val forecastInfo: LiveData<List<Forecast>> = _forecastInfo
    private val api = WeatherApiProvider.api

    fun getForecastOfCountry(coor : Pair<Double,Double>, appid: String) {
        val lat = coor.first
        val lon = coor.second
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val forecasts = api.getForecast(lat, lon, appid).list
                _forecastInfo.postValue(forecasts)
            } catch (e: Exception) {
                Log.e("TAG", e.message.orEmpty())
            }
        }
    }
}