package com.example.rainy.di

import com.example.rainy.data.datasource.IWeatherRemoteDataSource
import com.example.rainy.data.datasource.WeatherRemoteDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DataSourceModule {

    @Binds
    @Singleton
    abstract fun bindWeatherRemoteDataSource(
        dataSourceImpl: WeatherRemoteDataSourceImpl
    ) : IWeatherRemoteDataSource
}