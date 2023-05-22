package com.zabi.travelmemories.utils

data class WeatherResponse(
    val main: Main,
    // other required fields
)

data class Main(
    val temp: Double
)