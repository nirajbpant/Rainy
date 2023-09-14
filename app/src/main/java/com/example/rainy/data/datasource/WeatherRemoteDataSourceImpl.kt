package com.example.rainy.data.datasource

import com.example.rainy.data.WeatherService
import com.example.rainy.data.models.WeatherResponse
import retrofit2.Response
import javax.inject.Inject

class WeatherRemoteDataSourceImpl @Inject constructor(
    private val weatherService: WeatherService
) : IWeatherRemoteDataSource {

    override suspend fun getWeatherByCity(city: String): Response<WeatherResponse> {
        return weatherService.getWeatherByCity(city)
    }
}