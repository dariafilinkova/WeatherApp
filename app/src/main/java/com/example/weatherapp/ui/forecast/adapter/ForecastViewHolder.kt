package com.example.weatherapp.ui.forecast.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.data.forecast.Forecast
import com.example.weatherapp.data.forecast.ForecastResponse
import com.example.weatherapp.databinding.ItemForecastBinding

class ForecastViewHolder(item: View) : RecyclerView.ViewHolder(item) {
    private val bindingForecast = ItemForecastBinding.bind(item)
    fun bindForecast(forecast: Forecast) = with(bindingForecast) {
        textViewCondition.text = forecast.main.feels_like.toString()
        textViewTemperature.text=forecast.dt.toString()
    }
}