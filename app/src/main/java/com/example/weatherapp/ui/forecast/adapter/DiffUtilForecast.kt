package com.example.weatherapp.ui.forecast.adapter

import androidx.recyclerview.widget.DiffUtil
import com.example.weatherapp.data.forecast.Forecast

class DiffUtilForecast : DiffUtil.ItemCallback<Forecast>() {
    override fun areItemsTheSame(oldForecast: Forecast, newForecast: Forecast): Boolean {
        return oldForecast === newForecast
    }

    override fun areContentsTheSame(oldForecast: Forecast, newForecast: Forecast): Boolean {
        return oldForecast == newForecast
    }
}