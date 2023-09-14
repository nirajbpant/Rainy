package com.example.rainy.domain.repository

import android.content.Context
import coil.request.ImageResult
import com.example.rainy.data.models.WeatherResponse
import com.example.rainy.data.models.resource.Resource

interface IWeatherRepository {

    suspend fun  getCookieWeatherData() : Resource<WeatherResponse>?
    suspend fun  getWeatherByCity(city : String) : Resource<WeatherResponse>

    suspend fun loadWeatherConditionsImage(context: Context) : List<ImageResult>?

}