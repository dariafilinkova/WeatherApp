package com.example.weatherapp.data.current_weather


data class Main(
//    @SerializedName("feels_like")
    val feels_like: Double,

    val humidity: Int,
   // @SerializedName("pressure")
    val pressure: Int,
    val temp: Double,
    //@SerializedName("temp_max")
    val temp_max: Double,
   // @SerializedName("temp_min")
    val temp_min: Double
)