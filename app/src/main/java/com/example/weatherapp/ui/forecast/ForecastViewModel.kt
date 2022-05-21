package com.example.weatherapp.ui.forecast

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.api.WeatherApiProvider
import com.example.weatherapp.data.current_weather.CurrentWeatherResponse
import com.example.weatherapp.data.forecast.ForecastWeatherResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ForecastViewModel : ViewModel() {
    private val api = WeatherApiProvider.api
    private val _forecastInfo = MutableLiveData<ForecastWeatherResponse>()
    val forecastInfo: LiveData<ForecastWeatherResponse> = _forecastInfo

    fun getForecastOfCountry(coor : Pair<Double,Double>, appid: String) {
        val lat = coor.first
        val lon = coor.second
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _forecastInfo.postValue(api.getForecast(lat, lon, appid))
            } catch (e: Exception) {
                Log.e("TAG", e.message.orEmpty())
            }
        }
    }
}