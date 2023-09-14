package com.example.rainy.di

import android.content.Context
import com.example.rainy.domain.repository.IWeatherRepository
import com.example.rainy.domain.usecases.GetCookieWeatherData
import com.example.rainy.domain.usecases.GetWeatherByCity
import com.example.rainy.domain.usecases.GetWeatherIcons
import com.example.rainy.domain.usecases.WeatherUseCases
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object UseCasesModule {

    @Provides
    @Singleton
    fun provideNoteUseCases(repository: IWeatherRepository, context: Context) : WeatherUseCases{
        return WeatherUseCases(
            getWeatherByCity = GetWeatherByCity(repository),
            getCookieWeatherData = GetCookieWeatherData(repository),
            getWeatherIcons = GetWeatherIcons(repository, context)
        )
    }
}