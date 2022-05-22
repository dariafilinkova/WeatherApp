package com.example.weatherapp.data.forecast

import com.google.gson.annotations.SerializedName

data class OpenWeatherForecastResponse(
    @SerializedName("list")
    val forecastList : List<ForecastResponse>
)
