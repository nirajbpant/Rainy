package com.example.rainy.domain.usecases

import android.content.Context
import coil.request.ImageResult
import com.example.rainy.domain.repository.IWeatherRepository
import javax.inject.Inject

class GetWeatherIcons @Inject constructor(
    private val weatherRepository: IWeatherRepository,
    private val context: Context
) {

    suspend operator fun  invoke() : List<ImageResult>? {
         return weatherRepository.loadWeatherConditionsImage(context)
    }
}