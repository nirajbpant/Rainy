package com.example.rainy.data.api

import com.example.rainy.data.models.WeatherResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {

    //Todo Add URL & Headers
    @GET("")
    suspend fun getWeatherByCity(): Response<WeatherResponse>
}