package com.example.weatherapp.ui.forecast.adapter

import android.util.Log
import android.view.View
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.R
import com.example.weatherapp.data.forecast.Forecast
import com.example.weatherapp.data.forecast.ForecastResponse
import com.example.weatherapp.databinding.ItemForecastBinding
import java.text.SimpleDateFormat
import java.util.*

class ForecastViewHolder(item: View) : RecyclerView.ViewHolder(item) {
    private val bindingForecast = ItemForecastBinding.bind(item)
    fun bindForecast(forecast: Forecast) = with(bindingForecast) {
        temperature.text = (forecast.main.temp.toInt() - 273).toString() + "°"
        hour.text = forecast.dt_txt.substring(11, 16)
        skyConditionDescription.text = forecast.weather[0].main
        day.text =forecast.dt_txt.substring(0, 10)
            skyCondition.setBackgroundResource(
                when (forecast.weather[0].icon) {
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
    }
}