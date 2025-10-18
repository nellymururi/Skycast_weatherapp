package com.example.letemps.weather.data

data class Main(
    val temp: Double,
    val humidity: Int
)

data class Weather(
    val description: String
)

data class WeatherResponse(
    val name: String,
    val main: Main,
    val weather: List<Weather>,
    val dt: Long,
    val timezone: Long,
)