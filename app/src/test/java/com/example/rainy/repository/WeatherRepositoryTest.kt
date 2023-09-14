package com.example.rainy.repository

import com.example.rainy.data.models.Clouds
import com.example.rainy.data.models.Coordinate
import com.example.rainy.data.models.Main
import com.example.rainy.data.models.Sys
import com.example.rainy.data.models.Weather
import com.example.rainy.data.models.WeatherResponse
import com.example.rainy.data.models.Wind
import com.example.rainy.data.models.resource.Resource
import com.example.rainy.domain.repository.IWeatherRepository
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

class WeatherRepositoryTest {

    companion object{
        val weatherResponse = WeatherResponse(
            base = "SampleBase",
            clouds = Clouds(all = 5),
            cod = 200,
            coord = Coordinate(lat = 25.25, lon = 26.25),
            dt = 1631574613,
            id = 12345,
            main = Main(
                feels_like = 25.52,
                humidity = 5,
                pressure = 5,
                temp = 40.52,
                temp_max = 90.00,
                temp_min = 40.22
            ),
            name = "City1",
            sys = Sys(country = "Country1", id = 5, sunrise = 5, sunset = 10, type = 2),
            timezone = 3600,
            visibility = 10000,
            weather = listOf(Weather(id = 5, description = "Description123", icon = "5.png", main = "main1")),
            wind = Wind(deg = 5, speed = 2.5)
        )
    }

    @Mock
    private lateinit var weatherRepository: IWeatherRepository

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
    }

    @Test
     fun `test getWeatherByCity() success`() = runBlocking {
        val cityName = "City1"
        val expectedResult = Resource.Success(weatherResponse)
        runBlocking {
            `when`(weatherRepository.getWeatherByCity(cityName)).thenReturn(expectedResult)
        }
        val result = weatherRepository.getWeatherByCity(cityName)

        assert(result == expectedResult)
    }

    @Test
     fun `test getCookieWeatherData() success`() = runBlocking {
        val expectedResult = Resource.Success(weatherResponse)
        runBlocking {
            `when`(weatherRepository.getCookieWeatherData()).thenReturn(expectedResult)
        }
        val result = weatherRepository.getCookieWeatherData()

        assert(result == expectedResult)
    }
}
