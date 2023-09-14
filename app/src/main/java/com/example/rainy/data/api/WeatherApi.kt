package com.example.rainy.data.api

import com.example.rainy.BuildConfig
import com.example.rainy.data.models.WeatherResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {

    //Todo Add URL & Headers
    @GET("/data/2.5/weather")
    suspend fun getWeatherByCity(
        @Query("q")
        city: String,
        @Query("appid")
        api_key : String = BuildConfig.API_KEY
    ): Response<WeatherResponse>
}