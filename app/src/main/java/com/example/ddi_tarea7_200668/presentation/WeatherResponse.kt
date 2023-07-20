package com.example.ddi_tarea7_200668.presentation

data class WeatherResponse(
    val main: Main
)

data class Main(
    val temp: Float
)