package com.example.letemps

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import kotlinx.coroutines.runBlocking


//Data classes- essentially the data layer

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
    val weather: List<Weather>
)


//Defining the API service Interface
interface WeatherApi{
    @GET("data/2.5/weather")// to be edited based on the api key
    suspend fun getWeather(
        @Query("q") city: String,
        @Query("appid") apiKey: String,
        @Query("units") units: String = "metric"
    ): WeatherResponse
}

//Setup Retrofit Instance
object RetrofitClient {
    private const val BASE_URL = "https://api.openweathermap.org/"

    val instance: WeatherApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(WeatherApi::class.java)
    }
}

//  Run and test API call
fun main() = runBlocking {
    val apiKey = "70a4a7346e35ec80a2bf0beb195a065e"
    val city = "Nairobi"

    try {
        val response = RetrofitClient.instance.getWeather(city, apiKey)
        println("City: ${response.name}")
        println("Temperature: ${response.main.temp}°C")
        println("Humidity: ${response.main.humidity}%")
        println("Description: ${response.weather[0].description}")
    } catch (e: Exception) {
        println("Error: ${e.message}")
    }
}

