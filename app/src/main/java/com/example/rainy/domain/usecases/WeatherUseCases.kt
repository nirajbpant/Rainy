package com.example.rainy.domain.usecases

data class WeatherUseCases(
    val getWeatherByCity: GetWeatherByCity,
    val getCookieWeatherData: GetCookieWeatherData
)
