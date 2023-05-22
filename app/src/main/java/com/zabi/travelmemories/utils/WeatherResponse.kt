package com.zabi.travelmemories.utils

data class WeatherResponse(
    val main: Main,
)

data class Main(
    val temp: Double
)