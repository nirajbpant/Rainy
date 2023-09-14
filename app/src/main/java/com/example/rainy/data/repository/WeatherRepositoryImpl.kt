package com.example.rainy.data.repository

import android.util.Log
import com.example.rainy.data.SharedPreferencesManager
import com.example.rainy.data.datasource.IWeatherRemoteDataSource
import com.example.rainy.data.models.WeatherResponse
import com.example.rainy.data.models.resource.Resource
import com.example.rainy.domain.repository.IWeatherRepository
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException
import javax.inject.Inject

class WeatherRepositoryImpl @Inject constructor(
    private val weatherRemoteDataSource: IWeatherRemoteDataSource,
    private val sharedPreferencesManager: SharedPreferencesManager
): IWeatherRepository {

    companion object{
        const val cookieWeatherKey = "session:cookieWeatherKey"
        const val defaultVal = "default_value"
    }

    override suspend fun getCookieWeatherData(): Resource<WeatherResponse>? {
        val value = sharedPreferencesManager.getString(cookieWeatherKey, "default_value")
        Log.d("Default val", value)
        if (value!= defaultVal){
            return getWeatherData(value)
        }
        return null
    }

    override suspend fun getWeatherByCity(city: String): Resource<WeatherResponse> {
        return getWeatherData(city)
    }

    private suspend fun getWeatherData(city: String): Resource<WeatherResponse> {
        return try {
            val response = weatherRemoteDataSource.getWeatherByCity(city)
            sharedPreferencesManager.putString(cookieWeatherKey, city)
            responseToResult(response)
        } catch (e: HttpException) {
            Resource.Error("HTTP Error: ${e.code()}")
        } catch (e: IOException) {
            Resource.Error("Network Error: ${e.message}")
        } catch (e: Exception) {
            Resource.Error("An error occurred: ${e.message}")
        }
    }


    private fun responseToResult(response: Response<WeatherResponse>) : Resource<WeatherResponse> {
        if(response.isSuccessful){
            response.body()?.let {
                    result->
                return Resource.Success(result)
            }
        }
        return Resource.Error(response.message())
    }
}