package com.example.weatherapp.data.forecast

data class ForecastWeatherResponse(
    val city: City,
    val cnt: Int,
    val cod: String,
    val list: List<Forecast>,
    val message: Int
)