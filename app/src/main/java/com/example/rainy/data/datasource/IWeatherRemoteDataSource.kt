package com.example.rainy.data.datasource

import com.example.rainy.data.models.WeatherResponse
import retrofit2.Response

interface IWeatherRemoteDataSource {

    suspend fun  getWeatherByCity(city : String) : Response<WeatherResponse>
}