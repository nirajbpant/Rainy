package com.example.rainy.data

import com.example.rainy.data.api.WeatherApi
import retrofit2.Retrofit
import javax.inject.Inject

class WeatherService @Inject constructor(
    private val retrofit: Retrofit
) {

    private val weatherService by lazy { retrofit.create(WeatherApi::class.java) }

    suspend fun  getWeatherByCity(city : String) = weatherService.getWeatherByCity(city)
}