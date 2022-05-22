package com.example.weatherapp.ui.forecast.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.example.weatherapp.R
import com.example.weatherapp.data.forecast.Forecast

class ForecastAdapter : ListAdapter<Forecast, ForecastViewHolder>(DiffUtilForecast()) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ForecastViewHolder {
        return ForecastViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_forecast, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ForecastViewHolder, position: Int) {
        val forecast = getItem(position)
        holder.bindForecast(forecast)
    }

}