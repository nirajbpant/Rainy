package com.example.rainy.domain.usecases

import com.example.rainy.data.models.WeatherResponse
import com.example.rainy.data.models.resource.Resource
import com.example.rainy.domain.repository.IWeatherRepository
import javax.inject.Inject

class GetCookieWeatherData @Inject constructor(
    private val weatherRepository: IWeatherRepository
) {

    suspend operator fun  invoke() : Resource<WeatherResponse>? {
        return weatherRepository.getCookieWeatherData()
    }
}