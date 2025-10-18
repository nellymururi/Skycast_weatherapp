package com.example.letemps.weather.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.letemps.weather.data.LocationService
import com.example.letemps.weather.data.RetrofitClient
import com.example.letemps.weather.data.WeatherResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class WeatherViewModel(application: Application) : AndroidViewModel(application) {

    private val _weatherState = MutableStateFlow<WeatherResponse?>(null)
    val weatherState = _weatherState.asStateFlow()

    private val locationService = LocationService(application)
    private val apiKey = "70a4a7346e35ec80a2bf0beb195a065e"

    fun fetchWeatherByCity(city: String) {
        viewModelScope.launch {
            try {
                val response = RetrofitClient.instance.getWeatherByCity(city, apiKey)
                _weatherState.value = response
            } catch (e: Exception) {
                e.printStackTrace()
                _weatherState.value = null
            }
        }
    }

    fun fetchWeatherByCoordinates(lat: Double, lon: Double) {
        viewModelScope.launch {
            try {
                val response = RetrofitClient.instance.getWeatherByCoordinates(
                    lat = lat,
                    lon = lon,
                    apiKey = "70a4a7346e35ec80a2bf0beb195a065e"
                )
                _weatherState.value = response
            } catch (e: Exception) {
                e.printStackTrace()
                _weatherState.value = null
            }
        }
    }

    fun fetchWeatherByCurrentLocation() {
        viewModelScope.launch {
            try {
                val location = locationService.getCurrentLocation()
                if (location != null) {
                    val (lat, lon) = location
                    val response = RetrofitClient.instance.getWeatherByCoordinates(lat, lon, apiKey)
                    _weatherState.value = response
                }
            } catch (e: Exception) {
                e.printStackTrace()
                _weatherState.value = null
            }
        }
    }
}
