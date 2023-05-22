package com.zabi.travelmemories.utils

import retrofit2.Callback

class WeatherApiClient {
    private val apiKey = "3a4bb7dc818ee5a2f060b636426e7050"

    fun getWeatherData(latitude: Double, longitude: Double, callback: Callback<WeatherResponse>) {
        val service = WeatherService.create()
        val call = service.getWeatherData(latitude, longitude, apiKey)
        call.enqueue(callback)
    }
}