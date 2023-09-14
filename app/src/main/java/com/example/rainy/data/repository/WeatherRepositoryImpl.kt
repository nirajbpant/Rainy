package com.example.rainy.data.repository

import android.content.Context
import android.util.Log
import coil.ImageLoader
import coil.request.ImageRequest
import coil.request.ImageResult
import com.example.rainy.core.constants.Constants.weatherIcons
import com.example.rainy.data.SharedPreferencesManager
import com.example.rainy.data.datasource.IWeatherRemoteDataSource
import com.example.rainy.data.models.WeatherResponse
import com.example.rainy.data.models.resource.Resource
import com.example.rainy.domain.repository.IWeatherRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException
import javax.inject.Inject

class WeatherRepositoryImpl @Inject constructor(
    private val weatherRemoteDataSource: IWeatherRemoteDataSource,
    private val sharedPreferencesManager: SharedPreferencesManager,
    private val imageLoader: ImageLoader,
): IWeatherRepository {

    companion object{
        const val cookieWeatherKey = "session:cookieWeatherKey"
        const val defaultVal = "default_value"
    }


    /* Todo Given More Time this would be moved to its own Repository, Data Layer and UseCases
        and to a Splash Screen Compose and State
     */
    private suspend fun preloadAndCacheImage(imageLoader: ImageLoader, context: Context):List<ImageResult>? {
        var images = mutableListOf<ImageResult>()
        try {
            for (url in weatherIcons) {
                val request = ImageRequest.Builder(context)
                    .data(url)
                    .build()
                val result = withContext(Dispatchers.IO) {
                    imageLoader.execute(request)
                }
                images.add(result)
            }
        } catch (e: Exception) {
            return null
        }
        return images
    }


    override suspend fun loadWeatherConditionsImage(context: Context) : List<ImageResult>? {
        return preloadAndCacheImage(imageLoader, context)
    }

    override suspend fun getCookieWeatherData(): Resource<WeatherResponse>? {
        val value = sharedPreferencesManager.getString(cookieWeatherKey, "default_value")
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